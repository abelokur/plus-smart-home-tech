package ru.practicum.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.GeneralAvroSerializer;

import java.util.EnumMap;
import java.util.Properties;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Bean
    public EnumMap<TopicType, String> kafkaTopics() {
        EnumMap<TopicType, String> topics = new EnumMap<>(TopicType.class);
        topics.put(TopicType.TELEMETRY_SENSORS, "telemetry.sensors.v1");
        topics.put(TopicType.TELEMETRY_SNAPSHOTS, "telemetry.snapshots.v1");
        topics.put(TopicType.TELEMETRY_HUBS, "telemetry.hubs.v1");
        return topics;
    }

    @Bean
    public Properties kafkaProducerProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
        return properties;
    }
}
