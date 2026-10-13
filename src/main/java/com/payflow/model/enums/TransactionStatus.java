package com.payflow.model.enums;

public enum TransactionStatus {

    PENDING,        // Transaction created, waiting
    APPROVED,       // Bank approved ✅
    DECLINED,       // Bank declined ❌
    CAPTURED,       // Money captured from card
    SETTLED,        // Money reached merchant
    REVERSED,       // Transaction cancelled
    REFUNDED,       // Money sent back to customer
    TIMEOUT
}
