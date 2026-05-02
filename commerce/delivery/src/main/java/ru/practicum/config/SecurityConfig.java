package ru.practicum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import ru.practicum.security.BaseSecurityConfig;
import ru.practicum.security.BaseSecurityUsersProperties;

/**
 * Конфигурация безопасности приложения.
 * Наследует базовую конфигурацию и активирует Spring Security.
 *
 * <p>Использует in-memory аутентификацию с пользователями,
 * определенными в свойствах приложения.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends BaseSecurityConfig {

    public SecurityConfig(BaseSecurityUsersProperties usersProperties) {
        super(usersProperties);
    }
}