package ru.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Конфигурация безопасности для реактивного Spring WebFlux Gateway.
 * Настраивает аутентификацию и авторизацию для всех входящих запросов.
 *
 * <p>Используется в API Gateway на основе Spring Cloud Gateway.</p>
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Настраивает цепочку фильтров безопасности для реактивного приложения.
     * Определяет правила доступа к endpoint'ам и метод аутентификации.
     *
     * @param http объект ServerHttpSecurity для настройки
     * @return сконфигурированная цепочка фильтров безопасности
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorize ->
                        authorize
                                .pathMatchers("/actuator/**").permitAll()
                                .anyExchange().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }
}