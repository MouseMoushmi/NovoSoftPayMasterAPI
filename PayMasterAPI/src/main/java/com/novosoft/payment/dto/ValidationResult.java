package com.novosoft.payment.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private boolean valid;
    private List<String> errorMessages;

    public ValidationResult() {
        this.valid = true;
        this.errorMessages = new ArrayList<>();
    }

    public boolean isValid() {
        return valid;
    }

    public void addErrorMessage(String errorMessage) {
        this.valid = false;
        this.errorMessages.add(errorMessage);
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
