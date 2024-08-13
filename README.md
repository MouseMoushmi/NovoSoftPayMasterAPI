# NovoSoftPayMasterAPI

# Payment API Service

This project is a Spring Boot application for managing payment transactions, generating payment reports, and handling user authentication with JWT. It also supports features like refund processing, transaction history retrieval, and credit card validation.

## Table of Contents

1. [Getting Started](#getting-started)
2. [Database Schema](#database-schema)
3. [APIs](#apis)
4. [JWT Authentication](#jwt-authentication)
5. [Running Queries](#running-queries)
6. [Configuration](#configuration)
7. [Error Handling](#error-handling)

## Getting Started

### Prerequisites

- Java 17
- PostgreSQL
- Gradle

### Database Schema

spring.datasource.url=jdbc:postgresql://localhost:5432/paymentdb
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update

The application uses PostgreSQL as its database. Below is the schema for the payments table:

Table: payments

<img width="680" alt="image" src="https://github.com/user-attachments/assets/d88bb1a6-6a2c-4986-8b96-e5162762a5ff">

###  Create the payments table:

CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    transaction_id VARCHAR(50) UNIQUE NOT NULL,
    amount NUMERIC(10, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    customer_id VARCHAR(50) NOT NULL,
    card_holder_name VARCHAR(100) NOT NULL,
    card_number VARCHAR(16) NOT NULL
);


## APIs

  1. Authenticate User

Endpoint: [POST /api/authenticate](http://localhost:8080/api/authenticate)

Request Body: 
{
    "username": "moushmi",
    "password": ""
}

Response: 
{
    "status": 200,
    "message": "Authentication successful",
    "data": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtb3VzaG1pIiwiaWF0IjoxNzIzNTU0MjQwLCJleHAiOjE3MjM1OTAyNDB9.0_N1f2qQ31gIae6fH7FwdmqmS31gssioMBMe7gFUS7E"
}


  2. Post Payment

Endpoint: [[POST /api/payments](http://localhost:8080/api/payments)]

Request Body: 
{
    "amount": 150.00,
    "currency": "IND",
    "paymentMethod": "CREDIT_CARD",
    "customerId": "cust123",
    "cardNumber": "4111111111111111",
    "expiryDate": "12/24",
    "cvv": "123",
    "cardHolderName": "Moushmi D"
}

Response: 
{
    "status": 200,
    "message": "Payment processed successfully",
    "data": {
        "transactionId": "txn_1723571518728",
        "amount": 150.0,
        "currency": "USD",
        "status": "SUCCESS",
        "timestamp": "2024-08-13T23:21:58.728791"
    }
}

 3.Refund Payment

Endpoint: [[POST /api/refund](http://localhost:8080/api/refund)]

Request Body: 
{
        "transactionId": "txn_1723567766415",
        "amount": 1150.00   
}
Response: 
{
    "status": 200,
    "message": "Payment refunded successfully",
    "data": {
        "transactionId": "txn_1723568695324",
        "amount": 1150.0,
        "status": "REFUNDED",
        "timestamp": "2024-08-14T00:22:13.954777"
    }
}

  4.Payment History

Endpoint: [[GET /api/history](http://localhost:8080/api/history)]
Response: 
{
    "status": 200,
    "message": "Transaction history retrieved successfully",
    "data": {
        "content": [
            {
                "transactionId": "txn_1723571518728",
                "amount": 150.0,
                "currency": "USD",
                "status": "SUCCESS",
                "timestamp": "2024-08-13T23:21:58.728791"
            }
        ],
        "totalElements": 1,
        "totalPages": 1,
        "currentPage": 0
    }
}


4.Generate Payment Reports

Endpoint: [[GET /api/reports](http://localhost:8080/api/payments/reports?startDate=2024-08-01T00:00:00Z&endDate=2024-08-13T23:59:59Z&groupBy=week))]
Response: 
{
    "status": 200,
    "message": "Payment report generated successfully",
    "data": [
        {
            "interval": "2024-W33",
            "totalAmount": 2750.0,
            "transactionCount": 19,
            "averageTransactionValue": 144.73684210526315
        }
    ]
}


