package com.novosoft.payment.service;

import com.novosoft.payment.dto.PaymentRequest;
import com.novosoft.payment.dto.ValidationResult;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.regex.Pattern;

@Service
public class CardValidationService {

    private static final String CURRENCY_PATTERN = "USD|EUR|INR";
    private static final String PAYMENT_METHOD_PATTERN = "CREDIT_CARD|DEBIT_CARD|PAYPAL";
    private static final String EXPIRY_DATE_PATTERN = "(0[1-9]|1[0-2])/[0-9]{2}";

    public ValidationResult validateCreditCard(PaymentRequest creditCard) {
        ValidationResult validationResult = new ValidationResult();

        if (!isValidCurrency(creditCard.getCurrency())) {
            validationResult.addErrorMessage("Invalid currency. Must be USD, EUR, or INR.");
        }

        if (!isValidPaymentMethod(creditCard.getPaymentMethod())) {
            validationResult.addErrorMessage("Invalid payment method. Must be CREDIT_CARD, DEBIT_CARD, or PAYPAL.");
        }

        if (!isValidCustomerId(creditCard.getCustomerId())) {
            validationResult.addErrorMessage("Customer ID must not be blank.");
        }

        if (!isValidCardNumber(creditCard.getCardNumber())) {
            validationResult.addErrorMessage("Invalid card number. Must be 16 digits and pass the Luhn check.");
        }

        if (!isValidExpiryDate(creditCard.getExpiryDate())) {
            validationResult.addErrorMessage("Invalid expiry date. Must be in the format MM/YY and not in the past.");
        }

        if (!isValidCVV(creditCard.getCardNumber(), creditCard.getCvv())) {
            validationResult.addErrorMessage("Invalid CVV. Must be 3 or 4 digits depending on card type.");
        }

        return validationResult;
    }

    private boolean isValidCurrency(String currency) {
        return currency != null && Pattern.matches(CURRENCY_PATTERN, currency);
    }

    private boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod != null && Pattern.matches(PAYMENT_METHOD_PATTERN, paymentMethod);
    }

    private boolean isValidCustomerId(String customerId) {
        return customerId != null && !customerId.isBlank();
    }

    private boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || !Pattern.matches("\\d{16}", cardNumber)) {
            return false;
        }

        return isLuhnValid(cardNumber);
    }

    private boolean isValidExpiryDate(String expiryDate) {
        if (expiryDate == null || !Pattern.matches(EXPIRY_DATE_PATTERN, expiryDate)) {
            return false;
        }

        try {
            YearMonth expDate = YearMonth.parse(expiryDate, java.time.format.DateTimeFormatter.ofPattern("MM/yy"));
            YearMonth currentDate = YearMonth.now();
            return expDate.isAfter(currentDate);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidCVV(String cardNumber, String cvv) {
        if (cvv == null || cardNumber == null) {
            return false;
        }

        if (cardNumber.startsWith("34") || cardNumber.startsWith("37")) {
            return cvv.length() == 4 && Pattern.matches("\\d{4}", cvv);
        } else {
            return cvv.length() == 3 && Pattern.matches("\\d{3}", cvv);
        }
    }

    private boolean isLuhnValid(String cardNumber) {
        int[] digits = new int[cardNumber.length()];
        for (int i = 0; i < cardNumber.length(); i++) {
            digits[i] = Character.getNumericValue(cardNumber.charAt(i));
        }

        for (int i = digits.length - 2; i >= 0; i -= 2) {
            int doubleDigit = digits[i] * 2;
            if (doubleDigit > 9) {
                doubleDigit -= 9;
            }
            digits[i] = doubleDigit;
        }

        int sum = 0;
        for (int digit : digits) {
            sum += digit;
        }

        return sum % 10 == 0;
    }
}