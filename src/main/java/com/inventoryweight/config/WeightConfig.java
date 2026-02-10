package com.inventoryweight.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventoryweight.InventoryWeight;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class WeightConfig {
    private static WeightConfig INSTANCE;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("inventoryweight.json");
    
    public boolean enabled = true;
    public boolean showHud = true;
    
    public float hotbarWeightMultiplier = 1.25f;
    
    public boolean hardcoreMode = false;
    public float hardcoreMultiplier = 1.5f;
    
    public boolean enableLightTier = true;
    public boolean enableMediumTier = true;
    public boolean enableHeavyTier = true;
    public boolean enableOverloadedTier = true;
    public boolean enableCrippledTier = true;
    
    public float lightTierThreshold = 0.0f;
    public float mediumTierThreshold = 50.0f;
    public float heavyTierThreshold = 100.0f;
    public float overloadedTierThreshold = 175.0f;
    public float crippledTierThreshold = 250.0f;
    
    public float categoryWeightHeavy = 5.0f;
    public float categoryWeightMedium = 2.0f;
    public float categoryWeightLight = 0.5f;
    public float categoryWeightNearZero = 0.1f;
    
    public boolean horseIgnoresWeight = true;
    public boolean minecartIgnoresWeight = true;
    public boolean boatPartiallyIgnoresWeight = true;
    public float boatWeightReduction = 0.5f;
    
    public boolean showWeightInTooltip = true;
    public boolean weightAffectsSwimming = true;
    public boolean weightAffectsClimbing = true;
    public boolean weightAffectsFallDamage = true;
    public boolean weightCausesExhaustion = true;
    public boolean weightAffectsMining = true;
    public boolean weightAffectsAttackSpeed = true;
    
    public boolean shulkerBoxAddsContentWeight = true;
    public float shulkerBoxBaseWeight = 3.0f;
    public float shulkerContentWeightMultiplier = 0.5f;
    
    public boolean playTierChangeSounds = true;
    public boolean showTierChangeParticles = true;
    
    public boolean weightAffectsElytra = true;
    public boolean weightIncreasesDetection = true;
    public boolean weightIncreasesFootstepVolume = true;
    
    public boolean dimensionModifiersEnabled = true;
    public float overworldWeightMultiplier = 1.0f;
    public float netherWeightMultiplier = 1.15f;
    public float endWeightMultiplier = 0.85f;
    
    public boolean waterDepthAffectsWeight = true;
    public boolean rainAffectsWeight = true;
    public float rainWeightMultiplier = 1.1f;
    public boolean altitudeAffectsWeight = true;
    
    public boolean staminaSystemEnabled = false;
    public boolean showStaminaBar = true;
    public float staminaExhaustionRecoveryThreshold = 30.0f;
    
    public boolean applyStatusEffects = false;
    public boolean lightTierGivesSpeedBoost = false;
    
    public Map<String, Float> customItemWeights = new HashMap<>();
    
    public static WeightConfig getInstance() {
        if (INSTANCE == null) {
            load();
        }
        return INSTANCE;
    }
    
    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                INSTANCE = GSON.fromJson(json, WeightConfig.class);
                InventoryWeight.LOGGER.info("Configuration loaded from {}", CONFIG_PATH);
            } catch (IOException e) {
                InventoryWeight.LOGGER.error("Failed to load configuration", e);
                INSTANCE = new WeightConfig();
                save();
            }
        } else {
            INSTANCE = new WeightConfig();
            initializeDefaultCustomWeights();
            save();
        }
    }
    
    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            String json = GSON.toJson(INSTANCE);
            Files.writeString(CONFIG_PATH, json);
            InventoryWeight.LOGGER.info("Configuration saved to {}", CONFIG_PATH);
        } catch (IOException e) {
            InventoryWeight.LOGGER.error("Failed to save configuration", e);
        }
    }
    
    private static void initializeDefaultCustomWeights() {
        INSTANCE.customItemWeights.put("minecraft:anvil", 15.0f);
        INSTANCE.customItemWeights.put("minecraft:chipped_anvil", 15.0f);
        INSTANCE.customItemWeights.put("minecraft:damaged_anvil", 15.0f);
        INSTANCE.customItemWeights.put("minecraft:enchanting_table", 10.0f);
        INSTANCE.customItemWeights.put("minecraft:beacon", 8.0f);
        INSTANCE.customItemWeights.put("minecraft:lodestone", 12.0f);
        INSTANCE.customItemWeights.put("minecraft:netherite_block", 20.0f);
        INSTANCE.customItemWeights.put("minecraft:gold_block", 12.0f);
        INSTANCE.customItemWeights.put("minecraft:iron_block", 10.0f);
        INSTANCE.customItemWeights.put("minecraft:copper_block", 10.0f);
        INSTANCE.customItemWeights.put("minecraft:feather", 0.05f);
        INSTANCE.customItemWeights.put("minecraft:ender_pearl", 0.1f);
        INSTANCE.customItemWeights.put("minecraft:elytra", 0.5f);
    }
    
    public void reload() {
        load();
    }
}
