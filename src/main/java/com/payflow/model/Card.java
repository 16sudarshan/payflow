package com.payflow.model;

import com.payflow.model.enums.CardStatus;
import com.payflow.model.enums.CardType;

public class Card {

    // Card Identity
    private String cardNumber;
    private String cardHolderName;

    // Expiry
    private String expiryMonth;
    private String expiryYear;

    // Security
    private String cvv;
    private String pin;

    // Card Classification
    private CardType cardType;
    private CardStatus cardStatus;

    // Financial
    private double availableBalance;
    private double creditLimit;

    // Track wrong PIN attempts
    private int wrongPinAttempts;

    public Card(String cardNumber,
                String cardHolderName,
                String expiryMonth,
                String expiryYear,
                String cvv,
                String pin) {

        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvv = cvv;
        this.pin = pin;

        // Default values
        // When card is created:
        // Status is always ACTIVE
        // Wrong attempts always 0
        this.cardStatus = CardStatus.ACTIVE;
        this.wrongPinAttempts = 0;


    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(CardStatus cardStatus) {
        this.cardStatus = cardStatus;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public int getWrongPinAttempts() {
        return wrongPinAttempts;
    }

    public void setWrongPinAttempts(int wrongPinAttempts) {
        this.wrongPinAttempts = wrongPinAttempts;
    }


    public String getMaskedCardNumber() {

        if (cardNumber == null || cardNumber.length() < 16) {
            return "INVALID CARD";
        }

        String firstFour = cardNumber.substring(0, 4);
        String middle    = " **** **** ";
        String lastFour  = cardNumber.substring(12, 16);

        return firstFour + middle + lastFour;
    }

    @Override
    public String toString() {
        return "Card["
                + getMaskedCardNumber()  // Outside quotes!
                + " | "
                + cardType               // Outside quotes!
                + " | "
                + cardStatus             // Outside quotes!
                + " | "
                + cardHolderName
                + "]";   // Outside quotes!
    }







}