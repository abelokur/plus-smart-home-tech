package ru.practicum.repository;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Репозиторий для хранения снимков показаний датчиков в памяти.
 * Обеспечивает потокобезопасное хранение и доступ к данным с использованием ConcurrentHashMap.
 *
 * <p>Класс является компонентом Spring и может быть автоматически внедрен через механизм dependency injection.
 *
 * @see SensorsSnapshotAvro
 * @see ConcurrentHashMap
 */
@Component
public class InMemoryRepository {
    private final Map<String, SensorsSnapshotAvro> snapshots = new ConcurrentHashMap<>();

    /**
     * Возвращает снимок показаний датчиков для указанного хаба.
     *
     * <p>Если для указанного идентификатора хаба нет данных, возвращается пустой Optional.
     *
     * @param hubId идентификатор хаба, для которого запрашиваются данные
     * @return Optional содержащий {@link SensorsSnapshotAvro} если данные найдены,
     * или пустой Optional если данные отсутствуют
     * @throws NullPointerException если hubId равен null
     */
    public Optional<SensorsSnapshotAvro> get(String hubId) {
        return Optional.ofNullable(snapshots.get(hubId));
    }

    /**
     * Сохраняет снимок показаний датчиков в репозитории.
     *
     * <p>Данные сохраняются с использованием идентификатора хаба в качестве ключа.
     * Если для данного хаба уже существовали данные, они будут перезаписаны.
     *
     * @param sensorsSnapshotAvro объект снимка показаний датчиков для сохранения
     * @return сохраненный объект {@link SensorsSnapshotAvro}
     * @throws NullPointerException если sensorsSnapshotAvro или его hubId равен null
     */
    public SensorsSnapshotAvro put(SensorsSnapshotAvro sensorsSnapshotAvro) {
        snapshots.put(sensorsSnapshotAvro.getHubId(), sensorsSnapshotAvro);
        return sensorsSnapshotAvro;
    }
}
