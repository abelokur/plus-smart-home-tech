[file name]: README.MD
[file content begin]

# Smart Home Tech

Много модульная система умного дома, состоящая из нескольких сервисов для обработки телеметрии, управления сценариями,
электронной коммерции и взаимодействия с устройствами.

## Общая архитектура

Система состоит из нескольких независимых микросервисов, взаимодействующих через Kafka, gRPC и REST API:

### Телеметрия и мониторинг

- **Telemetry Collector** - собирает данные с устройств и отправляет в Kafka
- **Telemetry Aggregator** - агрегирует телеметрию для дальнейшей обработки
- **Telemetry Analyzer** - анализирует телеметрию, проверяет сценарии и отправляет команды
- **Telemetry Serialization** - модуль для сериализации/десериализации сообщений Kafka и gRPC взаимодействия

### Электронная коммерция

- **Gateway** - API Gateway для маршрутизации и балансировки запросов
- **Shopping Store** - управление магазином и каталогом товаров
- **Shopping Cart** - управление корзиной покупок
- **Order** - обработка заказов и управление их жизненным циклом
- **Payment** - обработка платежей
- **Delivery** - управление доставкой
- **Warehouse** - управление складом
- **Interaction API** - общий API модуль с базовыми зависимостями для всех сервисов коммерции

### Инфраструктура

- **Config Server** - централизованный сервер конфигураций для всех сервисов
- **Discovery Server** - сервер обнаружения сервисов на основе Eureka

## Модули проекта

### Инфраструктура (infra/)

- **config-server** - Сервер централизованной конфигурации на основе Spring Cloud Config. Позволяет микросервисам
  подключаться к внешним конфигурациям, что упрощает управление настройками в разных средах.
- **discovery-server** - Сервер обнаружения микросервисов на основе Spring Cloud Eureka. Позволяет микросервисам
  обнаруживать друг друга и взаимодействовать.

### Телеметрия (telemetry/)

- **collector** - Сервис сбора телеметрии с устройств умного дома. Отправляет данные в Kafka для дальнейшей обработки.
- **aggregator** - Сервис агрегации телеметрии. Собирает и группирует данные от collector для дальнейшей обработки.
- **analyzer** - Сервис анализа телеметрии умного дома. Обрабатывает снимки состояния сенсоров, проверяет условия
  сценариев и отправляет команды на выполнение действий.
- **serialization** - Модуль с Avro и Protobuf схемами для сериализации/десериализации сообщений Kafka и gRPC
  взаимодействия.
    - **avro-schemas** - Avro схемы для Kafka сообщений
    - **proto-schemas** - Protobuf схемы для gRPC взаимодействия

### Электронная коммерция (commerce/)

#### gateway

API Gateway на основе Spring Cloud Gateway. Обеспечивает:

- Маршрутизацию запросов к соответствующим микросервисам
- Балансировку нагрузки
- Аутентификацию и авторизацию через Spring Security
- Мониторинг через Spring Boot Actuator

#### interaction-api

Базовый модуль с общими зависимостями для всех сервисов коммерции:

- Spring Boot AOP для аспектно-ориентированного программирования
- Eureka Client для регистрации и обнаружения сервисов
- OpenFeign для декларативного REST клиентов
- Spring Boot Validation для валидации данных
- Spring Security для безопасности
- SpringDoc OpenAPI для документации API
- Lombok для сокращения boilerplate кода

### Витрина онлайн-магазина (shopping-store)

**Базовый путь:** `/api/v1/shopping-store`

1. **Управление товарами:**
    - **GET /** - Получение списка товаров по категории в пагинированном виде
        - Параметры: `category` (LIGHTING/CONTROL/SENSORS), `pageable` (пагинация)
        - Возвращает: массив `ProductDto`

    - **PUT /** - Создание нового товара в ассортименте
        - Тело: `ProductDto`
        - Возвращает: созданный `ProductDto` с присвоенным ID

    - **POST /** - Обновление товара в ассортименте
        - Тело: `ProductDto`
        - Возвращает: обновленный `ProductDto`

    - **POST /removeProductFromStore** - Удалить товар из ассортимента
        - Тело: ID товара (UUID)
        - Возвращает: boolean (успех операции)

    - **GET /{productId}** - Получить сведения по товару
        - Параметр: `productId` (UUID)
        - Возвращает: `ProductDto`

2. **Управление статусами:**
    - **POST /quantityState** - Установка статуса по товару (вызывается складом)
        - Тело: `SetProductQuantityStateRequest`
        - Возвращает: boolean

**Категории товаров:**

- **LIGHTING** - Освещение
- **CONTROL** - Управление
- **SENSORS** - Датчики

**Статусы товаров:**

- **ProductState**: ACTIVE, DEACTIVATE
- **QuantityState**: ENDED, FEW, ENOUGH, MANY

### Корзина покупок (shopping-cart)

**Базовый путь:** `/api/v1/shopping-cart`

1. **Управление корзиной:**
    - **GET /** - Получить актуальную корзину для авторизованного пользователя
        - Параметр: `username` (строка)
        - Возвращает: `ShoppingCartDto`

    - **PUT /** - Добавить товар в корзину
        - Параметр: `username` (строка)
        - Тело: объект с отображением productId → quantity
        - Возвращает: обновленный `ShoppingCartDto`

    - **DELETE /** - Деактивация корзины товаров для пользователя
        - Параметр: `username` (строка)

2. **Операции с товарами:**
    - **POST /remove** - Удалить указанные товары из корзины
        - Параметр: `username` (строка)
        - Тело: массив ID товаров (UUID)
        - Возвращает: обновленный `ShoppingCartDto`

    - **POST /change-quantity** - Изменить количество товаров в корзине
        - Параметр: `username` (строка)
        - Тело: `ChangeProductQuantityRequest`
        - Возвращает: обновленный `ShoppingCartDto`

### Управление складом (warehouse)

**Базовый путь:** `/api/v1/warehouse`

1. **Управление товарами на складе:**
    - **PUT /** - Добавить новый товар на склад
        - Тело: `NewProductInWarehouseRequest` (размеры, вес, хрупкость)
        - Возвращает: OK или ошибку если товар уже существует

    - **POST /add** - Принять товар на склад (увеличить количество)
        - Тело: `AddProductToWarehouseRequest` (productId, quantity)
        - Ошибка: если товар не найден на складе

    - **POST /return** - Принять возврат товаров на склад
        - Тело: объект с отображением productId → quantity

2. **Операции с заказами:**
    - **POST /check** - Предварительно проверить количество товаров для корзины
        - Тело: `ShoppingCartDto`
        - Возвращает: `BookedProductsDto` (вес, объем, хрупкость)

    - **POST /assembly** - Собрать товары к заказу для подготовки к отправке
        - Тело: `AssemblyProductsForOrderRequest`
        - Возвращает: `BookedProductsDto`

    - **POST /assembly/cancel** - отменить сборку товаров, вернуть товары на склад
        - Тело: orderId (UUID)

    - **POST /shipped** - Передать товары в доставку
        - Тело: `ShippedToDeliveryRequest` (orderId, deliveryId)

3. **Информация:**
    - **GET /address** - Предоставить адрес склада для расчёта доставки
        - Возвращает: `AddressDto`

### Обработка заказов (order)

**Базовый путь:** `/api/v1/order`

1. **Основные операции:**
    - **GET /** - Получить заказы пользователя
        - Параметр: `username` (строка)
        - Возвращает: массив `OrderDto`

    - **PUT /** - Создать новый заказ в системе
        - Тело: `CreateNewOrderRequest` (shoppingCart, deliveryAddress)
        - Возвращает: созданный `OrderDto`

2. **Управление статусами заказа:**
    - **POST /payment** - Оплата заказа
        - Тело: orderId (UUID)
        - Возвращает: `OrderDto` после оплаты

    - **POST /payment/failed** - Ошибка оплаты заказа
        - Тело: orderId (UUID)
        - Возвращает: `OrderDto` после ошибки оплаты

    - **POST /delivery** - Доставка заказа
        - Тело: orderId (UUID)
        - Возвращает: `OrderDto` после доставки

    - **POST /delivery/failed** - Ошибка доставки заказа
        - Тело: orderId (UUID)
        - Возвращает: `OrderDto` после ошибки доставки

    - **POST /assembly** - Сборка заказа
        - Тело: orderId (UUID)
        - Возвращает: `OrderDto` после сборки

    - **POST /assembly/failed** - Ошибка сборки заказа
        - Тело: orderId (UUID)
        - Возвращает: `OrderDto` после ошибки сборки

    - **POST /completed** - Завершение заказа
        - Тело: orderId (UUID)
        - Возвращает: завершенный `OrderDto`

3. Расчеты:
    - **POST /calculate/total** - Расчёт стоимости заказа
        - Тело: orderId (UUID)
        - Возвращает: `OrderDto` с расчетом общей стоимости

    - **POST /calculate/delivery** - Расчёт стоимости доставки заказа
        - Тело: orderId (UUID)
        - Возвращает: `OrderDto` с расчетом доставки

4. Возвраты:
    - **POST /return** - Возврат заказа
        - Тело: `ProductReturnRequest` (orderId, products)
        - Возвращает: `OrderDto` после возврата

#### Статусы заказа:

- **NEW, ON_PAYMENT, ON_DELIVERY, DONE, DELIVERED, ASSEMBLED, PAID, COMPLETED,
  DELIVERY_FAILED, ASSEMBLY_FAILED, PAYMENT_FAILED, PRODUCT_RETURNED, CANCELED**

### Платежный шлюз (payment)

**Базовый путь:** `/api/v1/payment`

1. **Операции с платежами:**
    - **POST /** - Формирование оплаты для заказа (переход в платежный шлюз)
        - Тело: `OrderDto`
        - Возвращает: `PaymentDto` с деталями оплаты

    - **POST /refund** - Эмуляция успешной оплаты (для тестирования)
        - Тело: paymentId (UUID)

    - **POST /failed** - Эмуляция отказа в оплате (для тестирования)
        - Тело: paymentId (UUID)

2. **Расчеты:**
    - **POST /totalCost** - Расчёт полной стоимости заказа
        - Тело: `OrderDto`
        - Возвращает: число (стоимость)

    - **POST /productCost** - Расчёт стоимости товаров в заказе
        - Тело: `OrderDto`
        - Возвращает: число (стоимость товаров)

### Шлюз доставки (delivery)

**Базовый путь:** `/api/v1/delivery`

1. **Управление доставками:**
    - **PUT /** - Создать новую доставку в БД
        - Тело: `DeliveryDto` (адреса, orderId, состояние)
        - Возвращает: созданный `DeliveryDto`

    - **PUT /cancel** - Отменить доставку
        - Тело: deliveryId (UUID)

2. **Эмуляция событий доставки:**
    - **POST /successful** - Эмуляция успешной доставки товара
        - Тело: orderId (UUID)

    - **POST /picked** - Эмуляция получения товара в доставку
        - Тело: orderId (UUID)

    - **POST /failed** - Эмуляция неудачного вручения товара
        - Тело: orderId (UUID)

3. **Расчеты:**
    - **POST /cost** - Расчёт полной стоимости доставки заказа
        - Тело: `OrderDto`
        - Возвращает: число (стоимость доставки)

#### Статусы доставки:

- **CREATED, IN_PROGRESS, DELIVERED, FAILED, CANCELLED**

## Технологии

### Основные технологии

- Java 21
- Spring Boot 3.5.7
- Spring Cloud 2025.0.0
- Maven для сборки

### Микросервисная инфраструктура

- Spring Cloud Config Server (централизованная конфигурация)
- Spring Cloud Netflix Eureka Server/Client (сервис обнаружения)
- Spring Cloud Gateway (API Gateway)
- Spring Cloud OpenFeign (REST клиенты)

### Базы данных

- PostgreSQL 42.7.7 (основная база данных)
- Spring Data JPA

### Межсервисное взаимодействие

- Apache Kafka 4.1.0 (асинхронная коммуникация)
- gRPC 1.63.0 (синхронная RPC коммуникация)
- Apache Avro 1.12.1 (сериализация для Kafka)
- Protocol Buffers 4.32.1 (сериализация для gRPC)

### Безопасность

- Spring Security
- Spring Boot Validation

### Документация и мониторинг

- SpringDoc OpenAPI 2.8.13 (документация API)
- Spring Boot Actuator (мониторинг и метрики)

### Утилиты

- Lombok 1.18.30 (сокращение boilerplate кода)
- MapStruct 1.6.3 (маппинг объектов)
- Resilience4j + Spring Retry (устойчивость и повторные попытки)

## Структура проекта

```bash
smart-home-tech/
├── infra/                           # Инфраструктурные сервисы
│   ├── config-server/               # Сервер конфигураций
│   └── discovery-server/            # Сервер обнаружения сервисов
├── telemetry/                       # Телеметрия и мониторинг
│   ├── aggregator/                  # Агрегатор телеметрии
│   ├── analyzer/                    # Анализатор телеметрии
│   ├── collector/                   # Сборщик телеметрии
│   └── serialization/               # Схемы сериализации
│       ├── avro-schemas/            # Avro схемы для Kafka
│       └── proto-schemas/           # Protobuf схемы для gRPC
└── commerce/                        # Электронная коммерция
    ├── gateway/                     # API Gateway
    ├── interaction-api/             # Базовый API модуль
    ├── shopping-store/              # Управление магазином
    ├── shopping-cart/               # Корзина покупок
    ├── warehouse/                   # Управление складом
    ├── order/                       # Обработка заказов
    ├── payment/                     # Обработка платежей
    └── delivery/                    # Управление доставкой
```

[file content end]