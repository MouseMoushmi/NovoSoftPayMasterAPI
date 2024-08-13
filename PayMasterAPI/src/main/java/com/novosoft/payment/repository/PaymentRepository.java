package com.novosoft.payment.repository;
import com.novosoft.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    List<Payment> findAllByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    Optional<Payment> findByTransactionId(String transactionId);
}