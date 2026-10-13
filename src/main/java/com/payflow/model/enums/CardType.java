package com.payflow.model.enums;

public enum CardType {

    VISA,          // Starts with 4
    MASTERCARD,    // Starts with 51-55
    AMEX,          // Starts with 34 or 37
    RUPAY,         // Starts with 60,65,81,82
    DISCOVER,      // Starts with 6011
    UNIONPAY,      // Starts with 62
    UNKNOWN        // Could not detect card type

}
