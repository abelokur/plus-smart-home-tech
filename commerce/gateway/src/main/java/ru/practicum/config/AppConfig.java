package ru.practicum.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;

/**
 * Конфигурация приложения Gateway.
 * Загружает настройки безопасности из конфигурации Spring Boot
 * и создает сервис аутентификации для реактивного Spring Security.
 *
 * <p>Настройки читаются из префикса "gateway" в application.yml/properties.</p>
 */
@ConfigurationProperties("gateway")
@Getter
@Setter
@ToString
public class AppConfig {

    /**
     * Имя пользователя для аутентификации в Gateway.
     */
    private String username;

    /**
     * Пароль пользователя для аутентификации в Gateway.
     * Используется NoOpPasswordEncoder ({noop} префикс).
     */
    private String password;

    /**
     * Роли пользователя, разделенные запятыми.
     * Пример: "ADMIN,USER" или просто "ADMIN"
     */
    private String roles;

    /**
     * Создает реактивный сервис деталей пользователя для Spring Security.
     * Использует in-memory аутентификацию с одним пользователем,
     * определенным в конфигурации приложения.
     *
     * @return MapReactiveUserDetailsService с настроенным пользователем
     */
    @Bean
    MapReactiveUserDetailsService userDetailsService() {
        var user = User.withUsername(username)
                .password("{noop}" + password)
                .roles(roles)
                .build();

        return new MapReactiveUserDetailsService(user);
    }
}