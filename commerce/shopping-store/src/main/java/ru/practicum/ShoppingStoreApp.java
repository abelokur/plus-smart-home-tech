package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Главный класс приложения магазина.
 * Запускает Spring Boot приложение с поддержкой микросервисной архитектуры.
 *
 * <p>Конфигурация включает:</p>
 * <ul>
 *   <li>Spring Boot автоконфигурацию</li>
 *   <li>Service Discovery для регистрации в Eureka/Consul</li>
 *   <li>AspectJ для AOP (логирование, метрики)</li>
 * </ul>
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@EnableCaching
@Slf4j
public class ShoppingStoreApp {

    public static void main(String[] args) {
        log.info("Starting ShoppingStoreApp");
        SpringApplication.run(ShoppingStoreApp.class, args);
        log.info("ShoppingStoreApp started");
    }
}