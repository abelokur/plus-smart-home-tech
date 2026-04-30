package ru.practicum.security;

import lombok.Data;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Базовая конфигурация пользователей для Spring Security.
 * Предоставляет абстрактный класс для хранения и преобразования
 * пользовательских данных из конфигурации в объекты UserDetails.
 *
 * <p>Дочерние классы должны определять конкретных пользователей
 * через Spring Boot properties/yaml конфигурацию.</p>
 */
@Data
public abstract class BaseSecurityUsersProperties {

    /**
     * Карта пользователей, где ключ - имя пользователя,
     * значение - конфигурация пользователя (пароль и роли).
     */
    private Map<String, UserConfig> users = new HashMap<>();

    /**
     * Конфигурация отдельного пользователя.
     * Содержит пароль и массив ролей пользователя.
     */
    @Data
    public static class UserConfig {

        /**
         * Пароль пользователя в чистом виде.
         * При преобразовании добавляется префикс {noop} для NoOpPasswordEncoder.
         */
        private String password;

        /**
         * Роли пользователя в системе.
         * Пример: ["USER", "ADMIN"]
         */
        private String[] roles;
    }

    /**
     * Преобразует конфигурацию пользователей в список объектов UserDetails.
     * Используется для настройки InMemoryUserDetailsManager в Spring Security.
     *
     * @return список объектов UserDetails для аутентификации
     */
    public List<UserDetails> toUserDetailsList() {
        return users.entrySet().stream()
                .map(entry -> User.withUsername(entry.getKey())
                        .password("{noop}" + entry.getValue().getPassword())
                        .roles(entry.getValue().getRoles())
                        .build())
                .toList();
    }
}