package com.payflow.model.enums;

public enum FraudRisk {
    LOW("Approve transaction"),
    MEDIUM("Approve with review"),
    HIGH("Decline transaction");  // ← semicolon needed!

    private String action;        // ← field needed!

    FraudRisk(String action) {    // ← constructor needed!
        this.action = action;
    }

    public String getAction() {   // ← getter needed!
        return action;
    }
}