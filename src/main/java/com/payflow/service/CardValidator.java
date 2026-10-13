package com.payflow.service;

import com.payflow.model.Card;
import com.payflow.model.enums.CardType;

public class CardValidator {

    // Detect card type from first digits
    public CardType detectCardType(String cardNumber) {

        // Visa → starts with 4
        if (cardNumber.startsWith("4")) {
            return CardType.VISA;
        }

        // Mastercard → starts with 51 to 55
        if (cardNumber.startsWith("51") ||
                cardNumber.startsWith("52") ||
                cardNumber.startsWith("53") ||
                cardNumber.startsWith("54") ||
                cardNumber.startsWith("55")) {
            return CardType.MASTERCARD;
        }

        // Amex → starts with 34 or 37
        if (cardNumber.startsWith("34") ||
                cardNumber.startsWith("37")) {
            return CardType.AMEX;
        }

        // RuPay → starts with 60, 65, 81, 82
        if (cardNumber.startsWith("60") ||
                cardNumber.startsWith("65") ||
                cardNumber.startsWith("81") ||
                cardNumber.startsWith("82")) {
            return CardType.RUPAY;
        }


        // Could not detect
        return CardType.UNKNOWN;

    }
    public boolean isValidCardNumber(String cardNumber) {

        // Step 1: Basic checks first
        if (cardNumber == null || cardNumber.length() != 16) {
            return false;
        }

        // Step 2: Check all characters are digits
        for (char c : cardNumber.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        // Step 3: Luhn Algorithm
        int sum = 0;
        boolean doubleDigit = false;

        // Go from RIGHT to LEFT
        for (int i = cardNumber.length() - 1; i >= 0; i--) {

            // Get current digit
            int digit = cardNumber.charAt(i) - '0';

            // Double every second digit
            if (doubleDigit) {
                digit = digit * 2;

                // If doubled digit > 9
                // Subtract 9
                if (digit > 9) {
                    digit = digit - 9;
                }
            }

            // Add to sum
            sum = sum + digit;

            // Flip the flag
            doubleDigit = !doubleDigit;
        }

        // Step 4: Check if divisible by 10
        return sum % 10 == 0;
    }
    // Add below isValidCardNumber() method

    public boolean isCardExpired(String expiryMonth,
                                 String expiryYear) {

        // Get current month and year
        // Java gives us this!
        java.time.LocalDate today = java.time.LocalDate.now();
        int currentMonth = today.getMonthValue();  // 1-12
        int currentYear  = today.getYear() % 100;  // Last 2 digits
        // 2024 → 24

        // Convert card expiry to numbers
        int cardMonth = Integer.parseInt(expiryMonth);
        int cardYear  = Integer.parseInt(expiryYear);

        // Check if expired
        // Card is expired if:
        // Card year < current year
        // OR card year = current year
        //    AND card month < current month
        if (cardYear < currentYear) {
            return true;   // Expired!
        }

        if (cardYear == currentYear &&
                cardMonth < currentMonth) {
            return true;   // Expired!
        }

        return false;  // Not expired
    }
    public boolean isValidCVV(String cvv, CardType cardType) {

        // CVV should not be null or empty
        if (cvv == null || cvv.isEmpty()) {
            return false;
        }

        // Check all characters are digits
        for (char c : cvv.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        // Amex CVV = 4 digits
        // All others = 3 digits
        if (cardType == CardType.AMEX) {
            return cvv.length() == 4;
        } else {
            return cvv.length() == 3;
        }
    }
}