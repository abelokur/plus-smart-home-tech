package ru.practicum.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dal.model.Scenario;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы со сценариями в базе данных.
 * Предоставляет методы для поиска и управления сценариями.
 */
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

    /**
     * Находит все сценарии для указанного хаба.
     *
     * @param hubId идентификатор хаба
     * @return список сценариев хаба
     */
    List<Scenario> findByHubId(String hubId);

    /**
     * Находит сценарий по идентификатору хаба и названию.
     *
     * @param hubId идентификатор хаба
     * @param name  название сценария
     * @return сценарий, если найден
     */
    Optional<Scenario> findByHubIdAndName(String hubId, String name);

    /**
     * Удаляет сценарий по идентификатору хаба и названию.
     *
     * @param hubId идентификатор хаба
     * @param name  название сценария для удаления
     */
    void deleteByHubIdAndName(String hubId, String name);
}
