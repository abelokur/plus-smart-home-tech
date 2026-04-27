# Smart Home Tech

Много модульная система умного дома, состоящая из нескольких сервисов для обработки телеметрии, управления сценариями и
взаимодействия с устройствами.

## Общая архитектура

Система состоит из нескольких независимых микросервисов, взаимодействующих через Kafka и gRPC:

- **Telemetry Collector** - собирает данные с устройств и отправляет в Kafka
- **Telemetry Aggregator** - агрегирует телеметрию для дальнейшей обработки
- **Telemetry Analyzer** - анализирует телеметрию, проверяет сценарии и отправляет команды
- **Telemetry Serialization** - модуль для сериализации/десериализации сообщений Kafka и gRPC взаимодействия
- **Config Server** - централизованный сервер конфигураций для всех сервисов
- **Discovery Server** - сервер обнаружения сервисов

## Модули проекта

### infra/config-server

Сервер централизованной конфигурации на основе Spring Cloud Config. Позволяет микросервисам подключаться к внешним
конфигурациям, что упрощает управление настройками в разных средах.

### infra/discovery-server

Сервер обнаружения микросервисов на основе Spring Cloud Eureka. Позволяет микросервисам обнаруживать друг друга и
взаимодействовать.

### telemetry/collector

Сервис сбора телеметрии с устройств умного дома. Принимает данные от устройств по HTTP и отправляет в Kafka.

### telemetry/aggregator

Сервис агрегации телеметрии. Собирает и группирует данные от collector для дальнейшей обработки.

### telemetry/analyzer

Сервис анализа телеметрии умного дома. Обрабатывает снимки состояния сенсоров, проверяет условия сценариев и отправляет
команды на выполнение действий.

### telemetry/serialization

Модуль с Avro и Protobuf схемами для сериализации/десериализации сообщений Kafka и gRPC взаимодействия.

## Технологии

- Java 21
- Spring Boot 3
- Spring Cloud Config
- Spring Cloud Netflix Eureka Server
- Kafka (межсервисное взаимодействие)
- PostgreSQL (хранение данных)
- gRPC (взаимодействие между сервисами)
- REST (внешнее API)
- Avro (схемы данных Kafka)
- Protobuf (схемы данных gRPC)
- Lombok
- Maven

## Структура проекта

```bash
plus-smart-home-tech/
├── infra/
│   └── config-server/   # Сервер конфигураций
│   └── discovery-server/   # Сервер обнаружения сервисов
├── telemetry/
│   ├── aggregator/       # Агрегатор телеметрии
│   ├── analyzer/        # Анализатор телеметрии
│   ├── collector/       # Сборщик телеметрии
│   └── serialization/     # Схемы сериализации (Avro/Protobuf)
└── docker-compose.yml   # Конфигурация для запуска DB и Kafka
```

## Запуск

### Требования

- Docker и Docker Compose
- Java 21
- Maven 3.8+

### Локальный запуск

1. Сборка проекта:

```bash
mvn clean install
```

2. Запуск инфраструктуры:

```bash
docker-compose up -d
```

3. Запуск сервисов:

Каждый сервис запускается отдельно в своей директории

```bash
cd infra/config-server && mvn spring-boot:run
cd infra/discovery-server && mvn spring-boot:run
cd telemetry/collector && mvn spring-boot:run
cd telemetry/aggregator && mvn spring-boot:run
cd telemetry/analyzer && mvn spring-boot:run
```

## Схемы данных

Используются Avro схемы из модуля telemetry/serialization/avro-schemas и Protobuf схемы из
telemetry/serialization/proto-schemas.

## Конфигурация

Все настройки сервисов централизованно управляются через Spring Cloud Config Server. Конфигурации хранятся во внешнем
репозитории (например, Git). Сервисы подключаются к Config Server при запуске и получают актуальные настройки.
Пример подключения в application.yml сервиса:

```yaml
spring:
  application:
    name: application-name
  config:
    import: configserver:http://localhost:8888
  cloud:
    config:
      fail-fast: true
      retry:
        useRandomPolicy: true
        max-interval: 6000
```

Настройки, предоставляемые Config Server:

- Подключение к Kafka
- Подключение к PostgreSQL
- Настройки gRPC клиентов/серверов
- Названия топиков Kafka