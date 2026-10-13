package com.payflow;

import com.payflow.model.Card;
import com.payflow.model.PaymentRequest;
import com.payflow.model.Transaction;
import com.payflow.model.enums.CardType;
import com.payflow.model.enums.EntryMode;
import com.payflow.model.enums.TransactionType;
import com.payflow.service.CardValidator;
import com.payflow.service.PaymentProcessor;

public class PayFlowApp {
    public static void main(String[] args) {

        // Create processor
        PaymentProcessor processor = new PaymentProcessor();
        CardValidator validator    = new CardValidator();

        // ── Create a Card ──────────────────────
        Card card = new Card(
                "4532015112830366",
                "John Doe",
                "12",
                "26",
                "123",
                "1234"
        );

        // Set card type using validator
        card.setCardType(
                validator.detectCardType("4532015112830366")
        );
        card.setAvailableBalance(5000.00);
        card.setCreditLimit(10000.00);

        // ── TEST 1: Valid Payment ──────────────
        System.out.println("=== TEST 1: Valid Payment ===");
        PaymentRequest request1 = new PaymentRequest();
        request1.setCardNumber("4532015112830366");
        request1.setExpiryMonth("12");
        request1.setExpiryYear("26");
        request1.setAmount(1500.00);
        request1.setCurrency("INR");
        request1.setTransactionType(TransactionType.PURCHASE);
        request1.setStan("123456");
        request1.setMerchantId("MERCH001");
        request1.setMerchantName("Amazon India");
        request1.setTerminalId("TERM001");
        request1.setEntryMode(EntryMode.CHIP);

        Transaction result1 = processor.processPayment(request1, card);
        System.out.println(result1);
        System.out.println("Balance after: " + card.getAvailableBalance());

        // ── TEST 2: Insufficient Funds ─────────
        System.out.println("\n=== TEST 2: Insufficient Funds ===");
        PaymentRequest request2 = new PaymentRequest();
        request2.setCardNumber("4532015112830366");
        request2.setExpiryMonth("12");
        request2.setExpiryYear("26");
        request2.setAmount(99999.00);  // Very high amount!
        request2.setCurrency("INR");
        request2.setTransactionType(TransactionType.PURCHASE);
        request2.setStan("123457");
        request2.setMerchantId("MERCH001");
        request2.setMerchantName("Amazon India");
        request2.setTerminalId("TERM001");
        request2.setEntryMode(EntryMode.CHIP);

        Transaction result2 = processor.processPayment(request2, card);
        System.out.println(result2);

        // ── TEST 3: Expired Card ───────────────
        System.out.println("\n=== TEST 3: Expired Card ===");
        PaymentRequest request3 = new PaymentRequest();
        request3.setCardNumber("4532015112830366");
        request3.setExpiryMonth("01");
        request3.setExpiryYear("23");  // Expired!
        request3.setAmount(500.00);
        request3.setCurrency("INR");
        request3.setTransactionType(TransactionType.PURCHASE);
        request3.setStan("123458");
        request3.setMerchantId("MERCH001");
        request3.setMerchantName("Amazon India");
        request3.setTerminalId("TERM001");
        request3.setEntryMode(EntryMode.CHIP);

        Transaction result3 = processor.processPayment(request3, card);
        System.out.println(result3);

        // ── TEST 4: Invalid Card Number ────────
        System.out.println("\n=== TEST 4: Invalid Card Number ===");
        PaymentRequest request4 = new PaymentRequest();
        request4.setCardNumber("1234567890123456"); // Invalid Luhn!
        request4.setExpiryMonth("12");
        request4.setExpiryYear("26");
        request4.setAmount(500.00);
        request4.setCurrency("INR");
        request4.setTransactionType(TransactionType.PURCHASE);
        request4.setStan("123459");
        request4.setMerchantId("MERCH001");
        request4.setMerchantName("Amazon India");
        request4.setTerminalId("TERM001");
        request4.setEntryMode(EntryMode.CHIP);

        Transaction result4 = processor.processPayment(request4, card);
        System.out.println(result4);
    }
}