package ru.practicum.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.dal.model.Sensor;

import java.util.Collection;
import java.util.Optional;

/**
 * Репозиторий для работы с сенсорами в базе данных.
 * Предоставляет методы для проверки существования и управления сенсорами.
 */
@Repository
public interface SensorRepository extends JpaRepository<Sensor, String> {

    /**
     * Проверяет существование всех указанных сенсоров для заданного хаба.
     *
     * @param ids   список идентификаторов сенсоров для проверки
     * @param hubId идентификатор хаба
     * @return true если все сенсоры существуют для указанного хаба
     */
    boolean existsByIdInAndHubId(Collection<String> ids, String hubId);

    /**
     * Находит сенсор по идентификатору и хабу.
     *
     * @param id    идентификатор сенсора
     * @param hubId идентификатор хаба
     * @return сенсор, если найден
     */
    Optional<Sensor> findByIdAndHubId(String id, String hubId);

    /**
     * Удаляет сенсор по идентификатору и хабу.
     *
     * @param sensorId идентификатор сенсора для удаления
     * @param hubId    идентификатор хаба
     */
    void deleteByIdAndHubId(String sensorId, String hubId);
}
