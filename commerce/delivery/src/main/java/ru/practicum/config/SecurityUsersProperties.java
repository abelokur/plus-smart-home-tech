package ru.practicum.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import ru.practicum.security.BaseSecurityUsersProperties;

/**
 * Конфигурация пользователей безопасности из свойств приложения.
 * Читает настройки пользователей из конфигурации Spring Boot
 * и автоматически обновляется при изменении конфигурации (RefreshScope).
 *
 * <p>Пример конфигурации в application.yml:</p>
 * <pre>
 * app:
 *   security:
 *     users:
 *       admin:
 *         password: admin123
 *         roles: ADMIN
 *       user:
 *         password: user123
 *         roles: USER
 * </pre>
 */
@Component
@ConfigurationProperties(prefix = "app.security")
@RefreshScope
public class SecurityUsersProperties extends BaseSecurityUsersProperties {

}