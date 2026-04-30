package ru.practicum.security;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Базовый класс конфигурации безопасности Spring Security.
 * Предоставляет общую настройку для аутентификации и авторизации,
 * используя in-memory пользователей и базовую HTTP аутентификацию.
 *
 * <p>Дочерние классы могут переопределять методы для кастомизации конфигурации.</p>
 */
@Data
@RequiredArgsConstructor
public abstract class BaseSecurityConfig {

    private final BaseSecurityUsersProperties usersProperties;

    /**
     * Создает менеджер пользователей в памяти на основе конфигурации.
     * Пользователи читаются из свойств приложения.
     *
     * @return InMemoryUserDetailsManager с настроенными пользователями
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager(usersProperties.toUserDetailsList());
    }

    /**
     * Настраивает цепочку фильтров безопасности.
     * Отключает CSRF, включает базовую HTTP аутентификацию
     * и настраивает правила доступа к endpoint'ам.
     *
     * @param http объект HttpSecurity для настройки
     * @return сконфигурированная цепочка фильтров
     * @throws Exception если произошла ошибка при конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(CsrfConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/actuator/**").permitAll()
                                .anyRequest().authenticated())
                .build();
    }
}