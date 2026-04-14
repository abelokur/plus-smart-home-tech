package ru.practicum.dal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dal.model.Scenario;
import ru.practicum.dal.model.mapper.ScenarioMapper;
import ru.practicum.dal.repository.ScenarioRepository;
import ru.practicum.dal.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления сценариями умного дома.
 * Обеспечивает создание, обновление, удаление и поиск сценариев.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;

    /**
     * Сохраняет или обновляет сценарий для указанного хаба.
     * Если сценарий с таким именем уже существует - обновляет его.
     *
     * @param hubId        идентификатор хаба
     * @param scenarioAvro Avro-событие с данными сценария
     * @return сохраненный или обновленный сценарий
     * @throws IllegalArgumentException если не все сенсоры существуют в указанном хабе
     */
    @Transactional
    public Scenario saveOrUpdate(String hubId, ScenarioAddedEventAvro scenarioAvro) {
        Scenario newScenario = ScenarioMapper.mapScenario(hubId, scenarioAvro);
        Optional<Scenario> existingScenario = scenarioRepository.findByHubIdAndName(hubId, newScenario.getName());

        List<String> deviceIds = scenarioAvro.getActions()
                .stream()
                .map(DeviceActionAvro::getSensorId)
                .toList();

        if (!sensorRepository.existsByIdInAndHubId(deviceIds, hubId)) {
            log.warn("Couldn't create scenario, not all sensors is exist in this hub {}", newScenario.getName());
            throw new IllegalArgumentException("Couldn't create scenario, not all sensors is exist in this hub");
        }

        Scenario savedScenario;
        if (existingScenario.isPresent()) {
            savedScenario = existingScenario.get();
            updateScenarioData(savedScenario, newScenario);
            log.info("Updated scenario: {} for hub: {}", newScenario.getName(), hubId);
        } else {
            savedScenario = newScenario;
            log.info("Created new scenario: {} for hub: {}", newScenario.getName(), hubId);
        }

        return scenarioRepository.save(savedScenario);
    }

    /**
     * Удаляет сценарий по идентификатору хаба и названию.
     *
     * @param hubId        идентификатор хаба
     * @param scenarioName название сценария для удаления
     */
    @Transactional
    public void deleteByHubIdAndName(String hubId, String scenarioName) {
        scenarioRepository.deleteByHubIdAndName(hubId, scenarioName);
    }

    /**
     * Обновляет данные существующего сценария новыми значениями.
     * Очищает старые условия и действия, затем добавляет новые.
     *
     * @param existing существующий сценарий для обновления
     * @param newData  новые данные сценария
     */
    private void updateScenarioData(Scenario existing, Scenario newData) {
        existing.getSensorActions().clear();
        existing.getSensorConditions().clear();

        existing.getSensorActions().putAll(newData.getSensorActions());
        existing.getSensorConditions().putAll(newData.getSensorConditions());
    }
}
