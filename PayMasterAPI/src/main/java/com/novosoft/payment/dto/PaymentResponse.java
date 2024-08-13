package com.novosoft.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)  // This will exclude fields with null values
public class PaymentResponse {

    private String transactionId;
    private Double amount;
    private String currency;
    private String status;
    private LocalDateTime timestamp;

    public PaymentResponse(String transactionId, Double amount, String currency, String status, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.timestamp = timestamp;
    }

    public PaymentResponse(String transactionId, Double amount,String status, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.status = status;
        this.timestamp = timestamp;
    }



    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}