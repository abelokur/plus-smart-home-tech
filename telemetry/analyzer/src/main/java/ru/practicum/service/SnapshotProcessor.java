package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.config.KafkaConfig;
import ru.practicum.config.TopicType;
import ru.practicum.dal.service.SnapshotService;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

/**
 * Компонент для обработки снимков состояния сенсоров из Kafka.
 * Читает сообщения из топика Kafka, анализирует состояние сенсоров
 * и запускает соответствующие сценарии при выполнении условий.
 */
@Component
@Slf4j
public class SnapshotProcessor {
    private final KafkaConsumer<String, SensorsSnapshotAvro> snapshotConsumer;
    private final KafkaConfig config;

    private final SnapshotService snapshotService;
    private final HubRouterProcessor hubRouterProcessor;

    private static final Duration POLL_DURATION = Duration.ofSeconds(3);

    /**
     * Конструктор компонента обработки снимков.
     *
     * @param config             конфигурация Kafka
     * @param snapshotService    сервис для обработки снимков и проверки сценариев
     * @param hubRouterProcessor процессор для отправки действий через gRPC
     */
    public SnapshotProcessor(KafkaConfig config, SnapshotService snapshotService, HubRouterProcessor hubRouterProcessor) {
        this.config = config;
        this.snapshotConsumer = new KafkaConsumer<>(config.getSnapshotConsumerProperties());
        this.snapshotService = snapshotService;
        this.hubRouterProcessor = hubRouterProcessor;
    }

    /**
     * Запускает обработку снимков состояния сенсоров.
     * Читает данные из Kafka топика, проверяет выполнение условий сценариев
     * и отправляет действия для выполнения через HubRouterProcessor.
     * Метод работает в бесконечном цикле до получения сигнала завершения.
     */
    public void start() {
        log.info("Starting SnapshotProcessor");
        String topic = config.getTopics().get(TopicType.TELEMETRY_SNAPSHOTS);

        try (snapshotConsumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(snapshotConsumer::wakeup));
            snapshotConsumer.subscribe(List.of(topic));

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = snapshotConsumer.poll(POLL_DURATION);

                if (records.count() > 0) {
                    log.debug("Processing {} snapshots", records.count());
                }

                records.forEach(this::executeActions);

                snapshotConsumer.commitSync();
            }

        } catch (WakeupException ignored) {
            log.info("Wakeup interrupted");
        } catch (Exception e) {
            log.error("Exception while trying to handle snapshot", e);
        } finally {
            try {
                if (snapshotConsumer != null) {
                    snapshotConsumer.commitSync();
                }
            } finally {
                log.info("Closing consumer");
                if (snapshotConsumer != null) {
                    snapshotConsumer.close();
                }
            }
        }
    }

    /**
     * Обрабатывает отдельный снимок состояния сенсоров.
     * Вызывает сервис для проверки условий сценариев и отправляет полученные действия на выполнение.
     *
     * @param record запись из Kafka топика с данными снимка состояния сенсоров
     */
    private void executeActions(ConsumerRecord<String, SensorsSnapshotAvro> record) {
        if (record.value() == null) {
            log.debug("Received null snapshot from topic: {}, partition: {}, offset: {}",
                    record.topic(), record.partition(), record.offset());
            return;
        }

        try {
            List<DeviceActionRequest> requests = snapshotService.handleSnapshot(record.value());

            if (!requests.isEmpty()) {
                log.info("Processing {} action requests from snapshot", requests.size());
                requests.forEach(hubRouterProcessor::handleAction);
                log.debug("Successfully processed {} action requests", requests.size());
            } else {
                log.debug("No action requests generated from snapshot");
            }
        } catch (Exception e) {
            log.error("Error processing snapshot from topic: {}, partition: {}, offset: {}",
                    record.topic(), record.partition(), record.offset(), e);
        }
    }
}
