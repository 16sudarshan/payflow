package com.payflow.model.enums;

public enum TransactionType {

    PURCHASE,
    REFUND,
    REVERSAL,
    PREAUTH,
    CAPTURE,        // Capture pre-authorized amount ← ADD
    VOID
}
