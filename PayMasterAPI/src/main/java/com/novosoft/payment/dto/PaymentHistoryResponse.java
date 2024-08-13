package com.novosoft.payment.dto;
import com.novosoft.payment.entity.Payment;

import java.util.List;

public class PaymentHistoryResponse {

    private List<PaymentResponse> content;
    private long totalElements;
    private int totalPages;
    private int currentPage;

    public PaymentHistoryResponse(List<Payment> payments, long totalElements, int totalPages, int currentPage) {
        this.content = payments.stream().map(payment -> new PaymentResponse(
                payment.getTransactionId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getTimestamp()
        )).toList();
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

    public List<PaymentResponse> getContent() {
        return content;
    }

    public void setContent(List<PaymentResponse> content) {
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
