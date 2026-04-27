package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Главный класс сервиса Analyzer.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@Slf4j
public class AnalyzerApp {
    public static void main(String[] args) {
        log.info("Starting analyzer service");
        SpringApplication.run(AnalyzerApp.class, args);
        log.info("Analyzer service started");
    }
}
