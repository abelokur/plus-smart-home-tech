package ru.practicum.dal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dal.model.Sensor;
import ru.practicum.dal.model.mapper.SensorMapper;
import ru.practicum.dal.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;

/**
 * Сервис для управления сенсорами умного дома.
 * Обеспечивает создание и удаление сенсоров.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SensorService {
    private final SensorRepository sensorRepository;

    /**
     * Сохраняет новый сенсор для указанного хаба.
     *
     * @param hubId      идентификатор хаба
     * @param deviceAvro Avro-событие с данными устройства
     * @return сохраненный сенсор
     */
    @Transactional
    public Sensor save(String hubId, DeviceAddedEventAvro deviceAvro) {
        Sensor sensor = SensorMapper.mapSensor(hubId, deviceAvro.getId());
        return sensorRepository.save(sensor);
    }

    /**
     * Удаляет сенсор по идентификатору и хабу.
     *
     * @param sensorId идентификатор сенсора для удаления
     * @param hubId    идентификатор хаба
     */
    @Transactional
    public void deleteByIdAndHubId(String sensorId, String hubId) {
        sensorRepository.deleteByIdAndHubId(sensorId, hubId);
    }
}
