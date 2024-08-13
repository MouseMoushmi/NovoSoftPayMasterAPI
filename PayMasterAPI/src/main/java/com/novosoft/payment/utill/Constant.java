package com.novosoft.payment.utill;

public class Constant {
    // Status Messages
    public static final String PAYMENT_SUCCESS = "Payment processed successfully";
    public static final String PAYMENT_VALIDATION_FAILED = "Payment validation failed";
    public static final String TRANSACTION_HISTORY_RETRIEVED = "Transaction history retrieved successfully";
    public static final String PAYMENT_REFUNDED = "Payment refunded successfully";
    public static final String PAYMENT_ALREADY_REFUNDED = "Payment has already been refunded";
    public static final String PAYMENT_NOT_ELIGIBLE_FOR_REFUND = "Payment is not eligible for a refund";
    public static final String TRANSACTION_NOT_FOUND = "Transaction not found or not eligible for refund";
    public static final String INVALID_GROUP_BY_PARAM = "Invalid groupBy parameter: ";
    public static final String PAYMENT_REPORT_GENERATED = "Payment report generated successfully";
    public static final String AUTHENTICATION_SUCCESSFUL = "Authentication successful";


    // JWT
    public static final String JWT_TOKEN_HEADER = "Authorization";
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String USER_NAME = "moushmi";
    public static final String PASSWORD = "$2b$12$Uk518IjEk.1Gup/t5g9iCerHZieNuaPOZJUnq2UQN4CXeLRaPDT5q";

    // Payment Status
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_REFUNDED = "REFUNDED";

    // Date Patterns
    public static final String DATE_PATTERN_ISO = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    // API Paths
    public static final String API_PATH_AUTHENTICATE = "/api/authenticate";


    // Others
    public static final String DEFAULT_GROUP_BY_DAY = "day";
    public static final String DEFAULT_GROUP_BY_WEEK = "week";
    public static final String DEFAULT_GROUP_BY_MONTH = "month";


    private Constant() {

    }
}
