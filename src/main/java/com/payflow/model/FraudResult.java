package com.payflow.model;

import com.payflow.model.enums.FraudRisk;
import java.util.ArrayList;

public class FraudResult {

    // Fields
    private int fraudScore;
    private FraudRisk  riskLevel;
    private ArrayList<String> triggeredRules;

    // Constructor
    public FraudResult(int fraudScore,
                       FraudRisk riskLevel,
                       ArrayList<String> triggeredRules) {
        this.fraudScore     = fraudScore;
        this.riskLevel      = riskLevel;
        this.triggeredRules = triggeredRules;
    }

    // Getters ONLY (no setters!)
    public int getFraudScore() {
        return fraudScore;
    }

    public FraudRisk getRiskLevel() {
        return riskLevel;
    }

    public ArrayList<String> getTriggeredRules() {
        return triggeredRules;
    }


    @Override
    public String toString() {
        return "FraudResult{" +
                "score="   + fraudScore     +
                ", risk="  + riskLevel      +
                ", rules=" + triggeredRules +
                "}";
    }
}