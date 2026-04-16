package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.WarehouseProduct;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Репозиторий для работы с товарами на складе.
 */
public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, UUID> {

    /**
     * Находит товары на складе по списку ID товаров.
     *
     * @param productIdList список ID товаров
     * @return список товаров на складе
     */
    List<WarehouseProduct> findAllByProductIdIn(Set<UUID> productIdList);

    /**
     * Находит товар на складе по ID товара.
     * При работе с несколькими складами может возвращать любой товар.
     *
     * @param productId ID товара
     * @return товар на складе
     */
    Optional<WarehouseProduct> findByProductId(UUID productId);
}