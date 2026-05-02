package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Payment;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностями Payment.
 * Предоставляет методы для доступа к данным о платежах в базе данных.
 */
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    /**
     * Находит платеж по его идентификатору.
     * Аналогичен стандартному методу findById, предоставлен для ясности API.
     *
     * @param paymentId идентификатор платежа
     * @return Optional с платежом, если найден, иначе пустой Optional
     */
    Optional<Payment> findPaymentByPaymentId(UUID paymentId);
}