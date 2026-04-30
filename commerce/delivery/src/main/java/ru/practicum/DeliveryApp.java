package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Главный класс приложения службы доставки.
 * Запускает Spring Boot приложение с поддержкой микросервисной архитектуры.
 *
 * <p>Конфигурация включает:</p>
 * <ul>
 *   <li>Spring Boot автоконфигурацию</li>
 *   <li>Feign клиенты для межсервисного взаимодействия</li>
 *   <li>Service Discovery для регистрации в Eureka/Consul</li>
 *   <li>AspectJ для AOP (логирование, метрики и т.д.)</li>
 * </ul>
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@Slf4j
public class DeliveryApp {
    public static void main(String[] args) {
        log.info("Starting DeliveryApp");
        SpringApplication.run(DeliveryApp.class, args);
        log.info("DeliveryApp started");
    }
}