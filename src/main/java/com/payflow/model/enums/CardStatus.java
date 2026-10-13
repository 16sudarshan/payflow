package com.payflow.model.enums;

public enum CardStatus {
    ACTIVE,      // Card working normally
    INACTIVE,    // Not yet activated
    BLOCKED,     // Wrong PIN 3 times
    LOST,        // Customer reported lost
    FROZEN,      // Fraud suspected - temp block
    EXPIRED,     // Past expiry date
    INVALID      // Account permanently closed
}
