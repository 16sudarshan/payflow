package com.payflow.service;

import com.payflow.model.Card;
import com.payflow.model.PaymentRequest;
import com.payflow.model.Transaction;
import com.payflow.model.enums.CardStatus;
import com.payflow.model.enums.CardType;
import com.payflow.model.enums.TransactionStatus;
import com.payflow.model.enums.TransactionType;
import com.payflow.model.FraudResult;
import com.payflow.model.enums.FraudRisk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentProcessor {

    // CardValidator to validate card details
    private CardValidator cardValidator;
    private FraudDetector fraudDetector;

    // Store all transactions
    // ArrayList = ordered list of transactions
    private List<Transaction> transactionHistory;

    // HashMap = quick search by transaction ID
    // Key   = transactionId
    // Value = Transaction object
    private Map<String, Transaction> transactionMap;

    // Constructor
    public PaymentProcessor() {
        this.cardValidator      = new CardValidator();
        this.fraudDetector      = new FraudDetector();
        this.transactionHistory = new ArrayList<>();
        this.transactionMap     = new HashMap<>();
    }
    public Transaction processPayment(
            PaymentRequest request,
            Card card) {

        // ── STEP 1: Validate Card Number ──────────
        if (!cardValidator.isValidCardNumber(
                request.getCardNumber())) {
            return createDeclinedTransaction(
                    request,
                    card,
                    "Invalid card number",
                    "14");   // Response code 14 = Invalid card
        }

        // ── STEP 2: Check Card Expiry ─────────────
        if (cardValidator.isCardExpired(
                request.getExpiryMonth(),
                request.getExpiryYear())) {
            return createDeclinedTransaction(
                    request,
                    card,
                    "Card expired",
                    "54");   // Response code 54 = Expired card
        }

        // ── STEP 3: Check Card Status ─────────────
        if (card.getCardStatus() == CardStatus.BLOCKED) {
            return createDeclinedTransaction(
                    request,
                    card,
                    "Card blocked",
                    "62");   // Response code 62 = Restricted card
        }

        if (card.getCardStatus() == CardStatus.LOST) {
            return createDeclinedTransaction(
                    request,
                    card,
                    "Card reported lost",
                    "41");   // Response code 41 = Lost card
        }

        if (card.getCardStatus() == CardStatus.FROZEN) {
            return createDeclinedTransaction(
                    request,
                    card,
                    "Card frozen",
                    "62");
        }

        // ── STEP 4: Validate Amount ───────────────
        if (request.getAmount() <= 0) {
            return createDeclinedTransaction(
                    request,
                    card,
                    "Invalid amount",
                    "13");   // Response code 13 = Invalid amount
        }

        // ── STEP 5: Check Balance ─────────────────
        if (request.getAmount() >
                card.getAvailableBalance()) {
            return createDeclinedTransaction(
                    request,
                    card,
                    "Insufficient funds",
                    "51");   // Response code 51 = Insufficient funds
        }
        FraudResult fraudResult = fraudDetector.checkFraud(
                card.getMaskedCardNumber(),
                request.getAmount()
        );

        // HIGH risk → Decline
        if (fraudResult.getRiskLevel() == FraudRisk.HIGH) {
            return createDeclinedTransaction(
                    request, card,
                    "Fraud suspected. " +
                            fraudResult.getTriggeredRules(),
                    "59"
            );
        }

        // MEDIUM risk → Approve with warning
        if (fraudResult.getRiskLevel() == FraudRisk.MEDIUM) {
            Transaction flaggedTxn =
                    createApprovedTransaction(request, card);
            flaggedTxn.setDeclineReason(
                    "WARNING: Suspicious activity. " +
                            fraudResult.getTriggeredRules()
            );
            return flaggedTxn;
        }


        // ── STEP 6: Approve Transaction ───────────
        return createApprovedTransaction(request, card);
    }
    // Add below processPayment() method

    private Transaction createDeclinedTransaction(
            PaymentRequest request,
            Card card,
            String declineReason,
            String responseCode) {

        // Create transaction object
        Transaction transaction = new Transaction(
                card.getMaskedCardNumber(),
                card.getCardType(),
                request.getExpiryMonth(),
                request.getExpiryYear(),
                request.getAmount(),
                request.getCurrency(),
                request.getTransactionType(),
                request.getStan(),
                request.getMerchantId(),
                request.getMerchantName(),
                request.getTerminalId()
        );

        // Set declined details
        transaction.setStatus(TransactionStatus.DECLINED);
        transaction.setDeclineReason(declineReason);
        transaction.setResponseCode(responseCode);

        // Store transaction
        saveTransaction(transaction);

        return transaction;
    }

    private Transaction createApprovedTransaction(
            PaymentRequest request,
            Card card) {

        // Create transaction object
        Transaction transaction = new Transaction(
                card.getMaskedCardNumber(),
                card.getCardType(),
                request.getExpiryMonth(),
                request.getExpiryYear(),
                request.getAmount(),
                request.getCurrency(),
                request.getTransactionType(),
                request.getStan(),
                request.getMerchantId(),
                request.getMerchantName(),
                request.getTerminalId()
        );

        // Set approved details
        transaction.setStatus(TransactionStatus.APPROVED);
        transaction.setResponseCode("00"); // 00 = Approved
        transaction.setAuthorizationCode(
                generateAuthCode()             // Generate auth code
        );

        // Deduct balance from card
        card.setAvailableBalance(
                card.getAvailableBalance() - request.getAmount()
        );

        // Store transaction
        saveTransaction(transaction);

        return transaction;
    }
    private void saveTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
        transactionMap.put(
                transaction.getTransactionId(),
                transaction
        );
        fraudDetector.recordTransaction(transaction);
    }

    // Generate random 6 digit auth code
    private String generateAuthCode() {
        int code = (int)(Math.random() * 900000) + 100000;
        return String.valueOf(code);
    }


}