package com.novosoft.payment.service;

import com.novosoft.payment.dto.*;
import com.novosoft.payment.entity.Payment;
import com.novosoft.payment.exception.InvalidGroupByException;
import com.novosoft.payment.exception.PaymentNotFoundException;
import com.novosoft.payment.repository.PaymentRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.novosoft.payment.utill.Constant.*;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CardValidationService cardValidationService;

    public ApiResponse<?> processPayment(PaymentRequest paymentRequest) {
        ValidationResult validationResult = cardValidationService.validateCreditCard(paymentRequest);
        if (!validationResult.isValid()) {
            savePayment(paymentRequest, "FAILED");
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), PAYMENT_VALIDATION_FAILED, validationResult.getErrorMessages());
        }

        Payment processedPayment = savePayment(paymentRequest, "SUCCESS");
        PaymentResponse paymentResponse = new PaymentResponse(
                processedPayment.getTransactionId(),
                processedPayment.getAmount(),
                processedPayment.getCurrency(),
                processedPayment.getStatus(),
                processedPayment.getTimestamp()
        );

        return new ApiResponse<>(HttpStatus.OK.value(), PAYMENT_SUCCESS, paymentResponse);
    }

    public ApiResponse<PaymentHistoryResponse> getTransactionHistory(PaymentRequest paymentRequest) {
        Pageable pageable = PageRequest.of(paymentRequest.getPage(), paymentRequest.getSize(), Sort.by("timestamp").descending());

        Specification<Payment> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (paymentRequest.getCustomerId() != null) {
                predicates.add(cb.equal(root.get("customerId"), paymentRequest.getCustomerId()));
            }
            if (paymentRequest.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), paymentRequest.getStatus()));
            }
            if (paymentRequest.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), paymentRequest.getStartDate()));
            }
            if (paymentRequest.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), paymentRequest.getEndDate()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Payment> paymentPage = paymentRepository.findAll(spec, pageable);
        PaymentHistoryResponse paymentHistoryResponse = new PaymentHistoryResponse(
                paymentPage.getContent(),
                paymentPage.getTotalElements(),
                paymentPage.getTotalPages(),
                paymentPage.getNumber()
        );

        return new ApiResponse<>(HttpStatus.OK.value(), TRANSACTION_HISTORY_RETRIEVED, paymentHistoryResponse);
    }

    public ApiResponse<PaymentResponse> refundPayment(String transactionId, Double amount) {
        Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(transactionId);

        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            if (STATUS_SUCCESS.equals(payment.getStatus())) {
                payment.setStatus(STATUS_REFUNDED);
                payment.setAmount(amount);
                payment.setTimestamp(LocalDateTime.now());
                Payment savedPayment = paymentRepository.save(payment);

                PaymentResponse paymentResponse = new PaymentResponse(
                        savedPayment.getTransactionId(),
                        savedPayment.getAmount(),
                        savedPayment.getStatus(),
                        savedPayment.getTimestamp()
                );

                return new ApiResponse<>(HttpStatus.OK.value(), PAYMENT_REFUNDED, paymentResponse);
            } else if (STATUS_REFUNDED.equals(payment.getStatus())) {
                return new ApiResponse<>(HttpStatus.OK.value(), PAYMENT_ALREADY_REFUNDED, null);
            } else {
                return new ApiResponse<>(HttpStatus.OK.value(), PAYMENT_NOT_ELIGIBLE_FOR_REFUND, null);

            }
        } else {
            return new ApiResponse<>(HttpStatus.OK.value(), TRANSACTION_NOT_FOUND, null);

        }
    }

    private Payment savePayment(PaymentRequest paymentRequest, String status) {
        Payment payment = new Payment(
                "txn_" + System.currentTimeMillis(),
                paymentRequest.getAmount(),
                paymentRequest.getCurrency(),
                status,
                LocalDateTime.now(),
                paymentRequest.getCustomerId(),
                paymentRequest.getCardNumber(),
                paymentRequest.getCardHolderName()
        );
        return paymentRepository.save(payment);
    }

    public ApiResponse<List<PaymentReportResponse>> generatePaymentReports(String startDate, String endDate, String groupBy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN_ISO);
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        List<Payment> payments = paymentRepository.findAllByTimestampBetween(start, end);
        List<PaymentReportResponse> report = new ArrayList<>();
        for (Map.Entry<String, List<Payment>> stringListEntry : payments.stream()
                .collect(Collectors.groupingBy(payment -> getGroupKey(payment.getTimestamp(), groupBy)))
                .entrySet()) {
            String interval = stringListEntry.getKey();
            List<Payment> groupedPayments = stringListEntry.getValue();
            double totalAmount = groupedPayments.stream().mapToDouble(Payment::getAmount).sum();
            long transactionCount = groupedPayments.size();
            double averageTransactionValue = totalAmount / transactionCount;
            PaymentReportResponse apply = new PaymentReportResponse(interval, totalAmount, transactionCount, averageTransactionValue);
            report.add(apply);
        }
        return new ApiResponse<>(HttpStatus.OK.value(), PAYMENT_REPORT_GENERATED, report);
    }

    private String getGroupKey(LocalDateTime timestamp, String groupBy) {
        return switch (groupBy.toLowerCase()) {
            case DEFAULT_GROUP_BY_DAY -> timestamp.toLocalDate().toString();
            case DEFAULT_GROUP_BY_WEEK -> timestamp.getYear() + "-W" + timestamp.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            case DEFAULT_GROUP_BY_MONTH -> timestamp.getYear() + "-" + timestamp.getMonthValue();
            default -> throw new InvalidGroupByException(INVALID_GROUP_BY_PARAM + groupBy);
        };
    }
}