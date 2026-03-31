package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Главный класс сервиса Aggregator.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@Slf4j
public class AggregatorApp {

    public static void main(String[] args) {
        log.info("Starting aggregation service");
        SpringApplication.run(AggregatorApp.class, args);
        log.info("Aggregation service started");
    }
}
