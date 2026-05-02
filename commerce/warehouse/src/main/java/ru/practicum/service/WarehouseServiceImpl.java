package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.practicum.client.ShoppingStoreFeignClient;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.product.QuantityState;
import ru.practicum.dto.product.SetProductQuantityStateRequest;
import ru.practicum.dto.warehouse.*;
import ru.practicum.exception.*;
import ru.practicum.mapper.AddressMapper;
import ru.practicum.mapper.DimensionMapper;
import ru.practicum.model.BookedProduct;
import ru.practicum.model.Warehouse;
import ru.practicum.model.WarehouseProduct;
import ru.practicum.repository.BookedProductRepository;
import ru.practicum.repository.WarehouseProductRepository;
import ru.practicum.repository.WarehouseRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Реализация сервиса управления складом.
 * Обрабатывает операции с товарами на складе: добавление, проверка наличия,
 * бронирование, сборка заказов и управление остатками.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    private final TransactionTemplate transactionTemplate;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseProductRepository warehouseProductRepository;
    private final BookedProductRepository bookedProductRepository;
    private final AddressMapper addressMapper;
    private final DimensionMapper dimensionMapper;
    private final ShoppingStoreFeignClient shoppingStoreClient;

    /**
     * Добавляет новый тип товара на склад.
     *
     * @param request данные о новом товаре
     * @throws SpecifiedProductAlreadyInWarehouseException если товар уже зарегистрирован на складе
     */
    @Transactional
    @Override
    public void addNewItem(NewProductInWarehouseRequest request) {
        Warehouse warehouse = getRandomWarehouse();
        UUID productId = UUID.fromString(request.productId());
        List<WarehouseProduct> products = warehouse.getProducts();

        if (products.stream()
                .anyMatch(p -> p.getProductId().equals(productId))) {
            throw new SpecifiedProductAlreadyInWarehouseException("Product already specified in warehouse, id = " +
                                                                  productId);
        }

        WarehouseProduct newProduct = WarehouseProduct.builder()
                .warehouse(warehouse)
                .productId(productId)
                .fragile(request.fragile())
                .dimensions(dimensionMapper.toEntity(request.dimension()))
                .weight(request.weight())
                .quantity(0L)
                .build();

        products.add(newProduct);
    }

    /**
     * Проверяет наличие товаров из корзины на складе.
     *
     * @param shoppingCart корзина для проверки
     * @return информация о забронированных товарах
     * @throws ProductInShoppingCartLowQuantityInWarehouse если товаров недостаточно на складе
     */
    @Transactional(readOnly = true)
    @Override
    public BookedProductsDto checkQuantityInWarehouse(ShoppingCartDto shoppingCart) {
        Map<UUID, Long> shoppingCartProducts = shoppingCart.products();
        List<WarehouseProduct> availableProducts = checkAvailableProducts(shoppingCartProducts);
        return determineBookedProducts(availableProducts, shoppingCartProducts);
    }

    /**
     * Добавляет количество существующего товара на склад.
     * Обновляет состояние количества товара в магазине.
     *
     * @param request запрос на добавление товара
     * @throws NoSpecifiedProductInWarehouseException если товар не найден на складе
     */
    @Override
    public void addItem(AddProductToWarehouseRequest request) {
        WarehouseProduct updatedWarehouseProduct = transactionTemplate.execute(status -> {
            WarehouseProduct warehouseProduct = warehouseProductRepository.findByProductId(request.productId())
                    .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                            String.format("Product which id = %s not found", request.productId())));

            warehouseProduct.setQuantity(warehouseProduct.getQuantity() + request.quantity());
            return warehouseProduct;
        });

        updateQuantityState(updatedWarehouseProduct);
    }

    /**
     * Собирает товары для заказа из корзины.
     * Бронирует товары на складе и обновляет остатки.
     *
     * @param request запрос на сборку товаров
     * @return информация о забронированных товарах с характеристиками доставки
     * @throws ProductInShoppingCartLowQuantityInWarehouse если товаров недостаточно на складе
     */
    @Override
    @Transactional
    public BookedProductsDto assemblyProductForOrder(AssemblyProductsForOrderRequest request) {
        Map<UUID, Long> assemblyProducts = request.products();
        List<WarehouseProduct> products = checkAvailableProducts(assemblyProducts);

        // Создаем бронирования
        List<BookedProduct> bookedProducts = products.stream()
                .map(product -> BookedProduct.builder()
                        .orderId(request.orderId())
                        .warehouseProduct(product)
                        .quantity(assemblyProducts.get(product.getProductId()))
                        .build())
                .toList();

        bookedProductRepository.saveAll(bookedProducts);

        // Уменьшаем количество
        products.forEach(product ->
                product.setQuantity(
                        product.getQuantity() -
                        assemblyProducts.get(product.getProductId())));

        products.forEach(this::updateQuantityState);

        return determineBookedProducts(products, assemblyProducts);
    }

    /**
     * Получает адрес склада.
     *
     * @return адрес склада
     */
    @Override
    public AddressDto getAddress() {
        Warehouse warehouse = getRandomWarehouse();
        return addressMapper.toDto(warehouse.getAddress());
    }

    /**
     * Отмечает товары как отгруженные для доставки.
     * Связывает забронированные товары с идентификатором доставки.
     *
     * @param request данные об отгрузке
     */
    @Override
    @Transactional
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        List<BookedProduct> bookedProducts =
                bookedProductRepository.findAllByOrderId(request.orderId());
        bookedProducts.forEach(product -> product.setDeliveryId(request.deliveryId()));
    }

    /**
     * Возвращает товары на склад.
     * Увеличивает остаток товаров и обновляет состояние количества.
     *
     * @param products товары для возврата (ID товара → количество)
     */
    @Override
    @Transactional
    public void returnToWarehouse(Map<UUID, Long> products) {
        List<WarehouseProduct> productsToReturn = warehouseProductRepository.findAllByProductIdIn(products.keySet());
        productsToReturn.forEach(product ->
                product.setQuantity(product.getQuantity() + products.get(product.getProductId())));
        productsToReturn.forEach(this::updateQuantityState);
    }

    /**
     * Отменяет сборку товаров для заказа.
     * Возвращает забронированные товары на склад и обнуляет бронирование.
     *
     * @param orderId идентификатор заказа
     */
    @Override
    @Transactional
    public void cancelAssemblyProductForOrder(UUID orderId) {
        List<BookedProduct> bookedProducts = bookedProductRepository.findAllByOrderId(orderId);

        if (bookedProducts.isEmpty()) {
            return;
        }

        List<WarehouseProduct> warehouseProductsToUpdate = new ArrayList<>();

        for (BookedProduct bookedProduct : bookedProducts) {
            WarehouseProduct warehouseProduct = bookedProduct.getWarehouseProduct();
            warehouseProduct.setQuantity(warehouseProduct.getQuantity() + bookedProduct.getQuantity());
            warehouseProductsToUpdate.add(warehouseProduct);
        }

        warehouseProductRepository.saveAll(warehouseProductsToUpdate);
        int canceledBooking = bookedProductRepository.updateQuantity(orderId, 0L);
        log.info("Canceled booking for {} booked products", canceledBooking);
        warehouseProductsToUpdate.forEach(this::updateQuantityState);
    }

    /**
     * Получает случайный склад из доступных.
     *
     * @return случайный склад
     * @throws RuntimeException если склады не найдены
     */
    private Warehouse getRandomWarehouse() {
        return warehouseRepository.findAll().stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("No warehouses found"));
    }

    /**
     * Обновляет состояние количества товара в магазине.
     * Отправляет запрос в сервис магазина для обновления статуса количества.
     *
     * @param warehouseProduct товар на складе
     */
    private void updateQuantityState(WarehouseProduct warehouseProduct) {
        if (warehouseProduct == null) {
            return;
        }

        QuantityState quantityState = determineQuantityState(warehouseProduct.getQuantity());
        log.debug("Setting quantity state for product {} to {}", warehouseProduct.getProductId(), quantityState);

        SetProductQuantityStateRequest request = new SetProductQuantityStateRequest(
                warehouseProduct.getProductId(),
                quantityState);

        try {
            shoppingStoreClient.setQuantityState(request);
        } catch (ResourceNotFoundException e) {
            log.warn("Product {} not in store", warehouseProduct.getProductId());
        } catch (BadRequestException e) {
            log.warn("Invalid quantity state for product {}", warehouseProduct.getProductId());
        } catch (ServiceTemporaryUnavailableException e) {
            log.warn("Service temporary unavailable {}", warehouseProduct.getProductId());
        } catch (Exception e) {
            log.warn("Unknown error {}", warehouseProduct.getProductId());
        }
    }

    /**
     * Определяет состояние количества на основе доступного количества.
     *
     * @param quantity доступное количество товара
     * @return состояние количества
     */
    private QuantityState determineQuantityState(Long quantity) {
        if (quantity == 0) {
            return QuantityState.ENDED;
        } else if (quantity < 10) {
            return QuantityState.FEW;
        } else if (quantity < 100) {
            return QuantityState.ENOUGH;
        } else {
            return QuantityState.MANY;
        }
    }

    /**
     * Проверяет доступность товаров на складе.
     *
     * @param checkingProducts товары для проверки (ID → требуемое количество)
     * @return доступные товары на складе
     * @throws ProductInShoppingCartLowQuantityInWarehouse если товаров недостаточно
     */
    private List<WarehouseProduct> checkAvailableProducts(Map<UUID, Long> checkingProducts) {
        if (checkingProducts == null || checkingProducts.isEmpty()) {
            return Collections.emptyList();
        }

        List<WarehouseProduct> products = warehouseProductRepository
                .findAllByProductIdIn(checkingProducts.keySet());

        Map<UUID, Long> availableProducts = products.stream()
                .collect(Collectors.toMap(
                        WarehouseProduct::getProductId,
                        WarehouseProduct::getQuantity,
                        Long::sum));

        List<UUID> notEnoughProducts = checkingProducts.entrySet().stream()
                .filter(entry -> {
                    Long available = availableProducts.get(entry.getKey());
                    return available == null || available < entry.getValue();
                }).map(Map.Entry::getKey)
                .toList();

        if (!notEnoughProducts.isEmpty()) {
            throw new ProductInShoppingCartLowQuantityInWarehouse("Out of stock products ids: { " +
                                                                  notEnoughProducts + " }");
        }

        return products;
    }

    /**
     * Определяет характеристики забронированных товаров для доставки.
     *
     * @param availableProducts доступные товары на складе
     * @param assemblyProducts  товары для сборки (ID → количество)
     * @return характеристики забронированных товаров
     */
    private BookedProductsDto determineBookedProducts(
            List<WarehouseProduct> availableProducts,
            Map<UUID, Long> assemblyProducts) {

        if (availableProducts == null || availableProducts.isEmpty()
            || assemblyProducts == null || assemblyProducts.isEmpty()) {
            return BookedProductsDto.builder().build();
        }

        Double deliveryWeight = availableProducts.stream()
                .mapToDouble(wp ->
                        wp.getWeight() * assemblyProducts.get(wp.getProductId()))
                .sum();

        Double deliveryVolume = availableProducts.stream()
                .mapToDouble(wp ->
                        wp.getDimensions().getVolume() * assemblyProducts.get(wp.getProductId()))
                .sum();

        Boolean fragile = availableProducts.stream().anyMatch(WarehouseProduct::getFragile);

        return BookedProductsDto.builder()
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .fragile(fragile)
                .build();
    }
}