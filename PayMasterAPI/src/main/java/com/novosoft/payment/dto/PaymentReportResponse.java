package com.novosoft.payment.dto;

public class PaymentReportResponse {

    private String interval;
    private double totalAmount;
    private long transactionCount;
    private double averageTransactionValue;

    public PaymentReportResponse(String interval, double totalAmount, long transactionCount, double averageTransactionValue) {
        this.interval = interval;
        this.totalAmount = totalAmount;
        this.transactionCount = transactionCount;
        this.averageTransactionValue = averageTransactionValue;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public double getAverageTransactionValue() {
        return averageTransactionValue;
    }

    public void setAverageTransactionValue(double averageTransactionValue) {
        this.averageTransactionValue = averageTransactionValue;
    }
}