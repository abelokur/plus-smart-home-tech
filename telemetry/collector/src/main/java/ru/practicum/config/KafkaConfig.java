package ru.practicum.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@ToString
@ConfigurationProperties("kafka")
@Slf4j
public class KafkaConfig {

    private EnumMap<TopicType, String> topics;
    private Properties producerProperties;

    public void setTopics(Map<String, String> topics) {
        this.topics = new EnumMap<>(TopicType.class);
        for (Map.Entry<String, String> entry : topics.entrySet()) {
            try {
                this.topics.put(TopicType.valueOf(entry.getKey()), entry.getValue());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid topic name {}", entry.getKey(), e);
            }
        }
    }
}
