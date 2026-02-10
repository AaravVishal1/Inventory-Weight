package com.inventoryweight.weight;

public enum WeightTier {
    LIGHT("Light", 0xFFFFFF, 0.0f, 50.0f),
    MEDIUM("Medium", 0xFFFF00, 50.0f, 100.0f),
    HEAVY("Heavy", 0xFFA500, 100.0f, 175.0f),
    OVERLOADED("Overloaded", 0xFF6600, 175.0f, 250.0f),
    CRIPPLED("Crippled", 0xFF0000, 250.0f, Float.MAX_VALUE);
    
    private final String displayName;
    private final int color;
    private final float minWeight;
    private final float maxWeight;
    
    WeightTier(String displayName, int color, float minWeight, float maxWeight) {
        this.displayName = displayName;
        this.color = color;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getColor() {
        return color;
    }
    
    public float getMinWeight() {
        return minWeight;
    }
    
    public float getMaxWeight() {
        return maxWeight;
    }
    
    public static WeightTier fromWeight(float weight) {
        for (WeightTier tier : values()) {
            if (weight >= tier.minWeight && weight < tier.maxWeight) {
                return tier;
            }
        }
        return CRIPPLED;
    }
    
    public float getMovementMultiplier() {
        return switch (this) {
            case LIGHT -> 1.0f;
            case MEDIUM -> 0.90f;
            case HEAVY -> 0.75f;
            case OVERLOADED -> 0.55f;
            case CRIPPLED -> 0.40f;
        };
    }
    
    public float getSprintMultiplier() {
        return switch (this) {
            case LIGHT -> 1.0f;
            case MEDIUM -> 0.95f;
            case HEAVY -> 0.70f;
            case OVERLOADED -> 0.0f;
            case CRIPPLED -> 0.0f;
        };
    }
    
    public float getJumpMultiplier() {
        return switch (this) {
            case LIGHT -> 1.0f;
            case MEDIUM -> 0.95f;
            case HEAVY -> 0.80f;
            case OVERLOADED -> 0.60f;
            case CRIPPLED -> 0.45f;
        };
    }
    
    public float getKnockbackResistanceModifier() {
        return switch (this) {
            case LIGHT -> 0.0f;
            case MEDIUM -> 0.0f;
            case HEAVY -> 0.0f;
            case OVERLOADED -> 0.0f;
            case CRIPPLED -> -0.2f;
        };
    }
    
    public float getSwimmingMultiplier() {
        return switch (this) {
            case LIGHT -> 1.0f;
            case MEDIUM -> 0.90f;
            case HEAVY -> 0.70f;
            case OVERLOADED -> 0.50f;
            case CRIPPLED -> 0.30f;
        };
    }
    
    public float getClimbingMultiplier() {
        return switch (this) {
            case LIGHT -> 1.0f;
            case MEDIUM -> 0.95f;
            case HEAVY -> 0.80f;
            case OVERLOADED -> 0.60f;
            case CRIPPLED -> 0.40f;
        };
    }
    
    public float getFallDamageMultiplier() {
        return switch (this) {
            case LIGHT -> 1.0f;
            case MEDIUM -> 1.0f;
            case HEAVY -> 1.15f;
            case OVERLOADED -> 1.35f;
            case CRIPPLED -> 1.5f;
        };
    }
    
    public float getExhaustionPerSecond() {
        return switch (this) {
            case LIGHT -> 0.0f;
            case MEDIUM -> 0.0f;
            case HEAVY -> 0.01f;
            case OVERLOADED -> 0.03f;
            case CRIPPLED -> 0.06f;
        };
    }
    
    public float getMiningSpeedMultiplier() {
        return switch (this) {
            case LIGHT -> 1.0f;
            case MEDIUM -> 0.95f;
            case HEAVY -> 0.85f;
            case OVERLOADED -> 0.70f;
            case CRIPPLED -> 0.50f;
        };
    }
    
    public float getAttackSpeedMultiplier() {
        return switch (this) {
            case LIGHT -> 1.0f;
            case MEDIUM -> 0.98f;
            case HEAVY -> 0.90f;
            case OVERLOADED -> 0.80f;
            case CRIPPLED -> 0.65f;
        };
    }
    
    public float getElytraSinkRate() {
        return switch (this) {
            case LIGHT -> 0.0f;
            case MEDIUM -> 0.002f;
            case HEAVY -> 0.006f;
            case OVERLOADED -> 0.012f;
            case CRIPPLED -> 0.02f;
        };
    }
    
    public double getDetectionRadiusBonus() {
        return switch (this) {
            case LIGHT -> 0.0;
            case MEDIUM -> 2.0;
            case HEAVY -> 5.0;
            case OVERLOADED -> 8.0;
            case CRIPPLED -> 12.0;
        };
    }
    
    public float getFootstepVolumeMultiplier() {
        return switch (this) {
            case LIGHT -> 0.8f;
            case MEDIUM -> 1.0f;
            case HEAVY -> 1.3f;
            case OVERLOADED -> 1.6f;
            case CRIPPLED -> 2.0f;
        };
    }
    
    public float getStaminaDrainRate() {
        return switch (this) {
            case LIGHT -> 0.1f;
            case MEDIUM -> 0.2f;
            case HEAVY -> 0.4f;
            case OVERLOADED -> 0.7f;
            case CRIPPLED -> 1.0f;
        };
    }
    
    public float getStaminaRegenRate() {
        return switch (this) {
            case LIGHT -> 0.5f;
            case MEDIUM -> 0.4f;
            case HEAVY -> 0.25f;
            case OVERLOADED -> 0.15f;
            case CRIPPLED -> 0.08f;
        };
    }
    
    public boolean canSprint() {
        return this == LIGHT || this == MEDIUM || this == HEAVY;
    }
    
    public int ordinalValue() {
        return this.ordinal();
    }
}
