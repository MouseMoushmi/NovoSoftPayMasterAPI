package com.novosoft.payment.controller;

import com.novosoft.payment.dto.*;
import com.novosoft.payment.entity.Payment;
import com.novosoft.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> processPayment(@RequestBody PaymentRequest paymentRequest) {
        ApiResponse<?> response = paymentService.processPayment(paymentRequest);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PaymentHistoryResponse>> getTransactionHistory(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setCustomerId(customerId);
        paymentRequest.setStatus(status);
        paymentRequest.setStartDate(startDate);
        paymentRequest.setEndDate(endDate);
        paymentRequest.setPage(page);
        paymentRequest.setSize(size);

        ApiResponse<PaymentHistoryResponse> response = paymentService.getTransactionHistory(paymentRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refund")
    public ResponseEntity<ApiResponse<PaymentResponse>> refundPayment(@RequestBody Payment payment) {
        ApiResponse<PaymentResponse> response = paymentService.refundPayment(payment.getTransactionId(), payment.getAmount());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/reports")
    public ResponseEntity<ApiResponse<List<PaymentReportResponse>>> generatePaymentReports(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String groupBy) {
        ApiResponse<List<PaymentReportResponse>> response = paymentService.generatePaymentReports(startDate, endDate, groupBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}