package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Warehouse;

import java.util.UUID;

/**
 * Репозиторий для работы со складами.
 */
public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {
}