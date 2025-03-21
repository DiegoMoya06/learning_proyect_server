package com.dm.learning.entities;

public enum WeightValue {
    HARD(1.5),
    MEDIUM(1.2),
    EASY(0.8),
    MAX(5.0),
    MIN(0.2);

    public final double weight;

    WeightValue(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}
