package ru.practicum.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Конфигурация кэширования приложения.
 * Настраивает Caffeine кэш для повышения производительности
 * за счет кэширования часто запрашиваемых данных.
 */
@Configuration
public class CacheConfig {

    /**
     * Создает конфигурацию Caffeine кэша.
     * Настраивает политику истечения срока действия и максимальный размер кэша.
     *
     * @return конфигурация Caffeine кэша
     */
    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES) // запись удаляется через 5 минут после последнего доступа
                .maximumSize(100); // максимальное количество записей в кэше
    }

    /**
     * Создает менеджер кэша на основе Caffeine.
     * Управляет жизненным циклом и настройками кэша.
     *
     * @param caffeine конфигурация Caffeine
     * @return менеджер кэша
     */
    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}