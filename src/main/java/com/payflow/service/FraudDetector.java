package com.payflow.service;

import com.payflow.model.FraudResult;
import com.payflow.model.Transaction;
import com.payflow.model.enums.FraudRisk;
import com.payflow.model.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FraudDetector {

    // HashMap stores transaction history per card
    // Key   = masked card number (String)
    // Value = list of transactions for that card
    // Used for velocity check and pattern detection
    private Map<String, List<Transaction>>
            cardTransactionHistory;

    // Rule 1: Max transactions allowed per minute
    // More than 3 = Suspicious velocity!
    private static final int MAX_TRANSACTIONS_PER_MINUTE = 3;

    // Rule 2: Amount above this = High amount flag
    // Sudden high amount = Suspicious!
    private static final double HIGH_AMOUNT_THRESHOLD = 100000.00;

    // Rule 3: Amount below this = Card testing
    // Very small amounts = Testing stolen card!
    private static final double CARD_TESTING_AMOUNT = 10.00;

    // Rule 4: Max declines allowed per card
    // Too many declines = Suspicious!
    private static final int MAX_DECLINES_PER_CARD = 3;

    // Constructor
    public FraudDetector() {
        this.cardTransactionHistory = new HashMap<>();
    }

    // ── RULE 1: Velocity Check ─────────────────
    // Same card used more than 3 times in 1 minute
    // Normal customer does NOT do this!
    private boolean isVelocityFraud(String maskedCardNumber) {

        // Get history for this card
        // If card not found return empty list
        List<Transaction> history = cardTransactionHistory
                .getOrDefault(maskedCardNumber, new ArrayList<>());

        // Get time 1 minute ago
        LocalDateTime oneMinuteAgo = LocalDateTime
                .now()
                .minusMinutes(1);

        // Count transactions in last 1 minute
        // Using Java 8 Streams!
        long recentCount = history.stream()
                .filter(t -> t.getTimestamp()
                        .isAfter(oneMinuteAgo))
                .count();

        // More than 3 in 1 minute = FRAUD
        return recentCount >= MAX_TRANSACTIONS_PER_MINUTE;
    }

    // ── RULE 2: High Amount Check ──────────────
    // Sudden very high amount = Suspicious
    // Example: Card normally used for small amounts
    // Suddenly ₹100000 transaction appears!
    private boolean isHighAmountSuspicious(double amount) {
        return amount > HIGH_AMOUNT_THRESHOLD;
    }

    // ── RULE 3: Card Testing Pattern ──────────
    // Fraudster tests stolen card with tiny amounts
    // Example: ₹1, ₹1, ₹1 → then big transaction!
    private boolean isCardTestingPattern(
            String maskedCardNumber) {

        List<Transaction> history = cardTransactionHistory
                .getOrDefault(maskedCardNumber, new ArrayList<>());

        // Check last 5 minutes for tiny transactions
        LocalDateTime fiveMinutesAgo = LocalDateTime
                .now()
                .minusMinutes(5);

        // Count very small transactions
        long smallTxnCount = history.stream()
                .filter(t -> t.getTimestamp()
                        .isAfter(fiveMinutesAgo))
                .filter(t -> t.getAmount()
                        <= CARD_TESTING_AMOUNT)
                .count();

        // 2 or more tiny transactions = Card testing!
        return smallTxnCount >= 2;
    }

    // ── RULE 4: Multiple Declines ──────────────
    // Same card declined many times = Suspicious
    // Could be fraudster trying different amounts
    private boolean isMultipleDeclines(
            String maskedCardNumber) {

        List<Transaction> history = cardTransactionHistory
                .getOrDefault(maskedCardNumber, new ArrayList<>());

        // Check declines in last 10 minutes
        LocalDateTime tenMinutesAgo = LocalDateTime
                .now()
                .minusMinutes(10);

        long declineCount = history.stream()
                .filter(t -> t.getTimestamp()
                        .isAfter(tenMinutesAgo))
                .filter(t -> t.getStatus() ==
                        TransactionStatus.DECLINED)
                .count();

        return declineCount >= MAX_DECLINES_PER_CARD;
    }

    // ── MAIN METHOD: Check All Rules ───────────
    // Runs ALL 4 rules and returns FraudResult
    // Each rule adds points to fraud score
    // Higher score = More suspicious!
    public FraudResult checkFraud(
            String maskedCardNumber,
            double amount) {

        int fraudScore = 0;
        ArrayList<String> triggeredRules = new ArrayList<>();

        // Check Rule 1
        if (isVelocityFraud(maskedCardNumber)) {
            fraudScore += 40;
            triggeredRules.add("VELOCITY_BREACH");
        }

        // Check Rule 2
        if (isHighAmountSuspicious(amount)) {
            fraudScore += 30;
            triggeredRules.add("HIGH_AMOUNT");
        }

        // Check Rule 3
        if (isCardTestingPattern(maskedCardNumber)) {
            fraudScore += 50;
            triggeredRules.add("CARD_TESTING");
        }

        // Check Rule 4
        if (isMultipleDeclines(maskedCardNumber)) {
            fraudScore += 40;
            triggeredRules.add("MULTIPLE_DECLINES");
        }

        // Determine risk level from score
        // 0      = No fraud = LOW
        // 1-40   = Some risk = MEDIUM
        // 41+    = High risk = HIGH
        FraudRisk risk;
        if (fraudScore == 0) {
            risk = FraudRisk.LOW;
        } else if (fraudScore < 40) {
            risk = FraudRisk.MEDIUM;
        } else {
            risk = FraudRisk.HIGH;
        }

        return new FraudResult(fraudScore, risk, triggeredRules);
    }

    // ── RECORD TRANSACTION ─────────────────────
    // Called after every transaction
    // Saves to history for future fraud checks
    // computeIfAbsent creates new list if card
    // not seen before!
    public void recordTransaction(Transaction transaction) {
        String cardNumber = transaction.getMaskedCardNumber();

        cardTransactionHistory
                .computeIfAbsent(cardNumber,
                        k -> new ArrayList<>())
                .add(transaction);
    }
}
