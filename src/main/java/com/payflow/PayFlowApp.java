package com.payflow;

import com.payflow.model.enums.CardType;
import com.payflow.service.CardValidator;

import com.payflow.model.Transaction;
import com.payflow.model.enums.CardType;
import com.payflow.model.enums.TransactionType;

public class PayFlowApp {
    public static void main(String[] args) {

        // Create validator
        /*CardValidator validator = new CardValidator();

        // Test different card numbers
        String visaCard       = "4532015112830366";
        String mastercardCard = "5123456789012346";
        String amexCard       = "371449635398431";
        String rupayCard      = "6072015112830366";

        // Detect card types
        System.out.println(validator.detectCardType(visaCard));
        System.out.println(validator.detectCardType(mastercardCard));
        System.out.println(validator.detectCardType(amexCard));
        System.out.println(validator.detectCardType(rupayCard));
        System.out.println(validator.isValidCardNumber("4532015112830366")); // true
        System.out.println(validator.isValidCardNumber("1234567890123456")); // false
        System.out.println(validator.isValidCardNumber("123"));
        System.out.println(validator.isCardExpired("06", "23")); // true - expired
        System.out.println(validator.isCardExpired("12", "26")); // false - valid
        System.out.println(validator.isCardExpired("01", "24")); // true - expired// fals
        System.out.println(validator.isValidCVV("123", CardType.VISA));      // true
        System.out.println(validator.isValidCVV("12", CardType.VISA));       // false
        System.out.println(validator.isValidCVV("1234", CardType.AMEX));     // true
        System.out.println(validator.isValidCVV("123", CardType.AMEX));      // false
        System.out.println(validator.isValidCVV("abc", CardType.VISA));      // false
*/
        Transaction transaction = new Transaction(
                "4532 **** **** 0366",
                CardType.VISA,
                "12",
                "26",
                1500.00,
                "INR",
                TransactionType.PURCHASE,
                "123456",
                "MERCH001",
                "Amazon India",
                "TERM001"
        );
        System.out.println(transaction);
        System.out.println(transaction.getTransactionId());
        System.out.println(transaction.getStatus());
    }
}