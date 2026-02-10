package com.inventoryweight.weight;

public enum WeightCategory {
    HEAVY(5.0f),
    MEDIUM(2.0f),
    LIGHT(0.5f),
    NEAR_ZERO(0.1f),
    DEFAULT(1.0f);
    
    private final float baseWeight;
    
    WeightCategory(float baseWeight) {
        this.baseWeight = baseWeight;
    }
    
    public float getBaseWeight() {
        return baseWeight;
    }
}
