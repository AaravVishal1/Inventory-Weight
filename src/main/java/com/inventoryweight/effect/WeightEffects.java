package com.inventoryweight.effect;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.weight.WeightManager;
import com.inventoryweight.weight.WeightTier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class WeightEffects {
    
    public static void applyTierEffects(PlayerEntity player) {
        WeightConfig config = WeightConfig.getInstance();
        if (!config.enabled || !config.applyStatusEffects) {
            return;
        }
        
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }
        
        WeightTier tier = WeightManager.getPlayerTier(player);
        
        switch (tier) {
            case CRIPPLED -> applyCrippledEffects(player);
            case OVERLOADED -> applyOverloadedEffects(player);
            case LIGHT -> applyLightEffects(player);
            default -> {}
        }
    }
    
    private static void applyCrippledEffects(PlayerEntity player) {
        if (!player.hasStatusEffect(StatusEffects.SLOWNESS)) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    60,
                    0,
                    true,
                    false,
                    true
            ));
        }
        
        if (!player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.MINING_FATIGUE,
                    60,
                    0,
                    true,
                    false,
                    true
            ));
        }
    }
    
    private static void applyOverloadedEffects(PlayerEntity player) {
        if (!player.hasStatusEffect(StatusEffects.SLOWNESS)) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    40,
                    0,
                    true,
                    false,
                    false
            ));
        }
    }
    
    private static void applyLightEffects(PlayerEntity player) {
        WeightConfig config = WeightConfig.getInstance();
        if (!config.lightTierGivesSpeedBoost) {
            return;
        }
        
        float weight = WeightManager.getPlayerWeight(player);
        if (weight < 10.0f && !player.hasStatusEffect(StatusEffects.SPEED)) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SPEED,
                    40,
                    0,
                    true,
                    false,
                    false
            ));
        }
    }
    
    public static void removeAllWeightEffects(PlayerEntity player) {
        player.removeStatusEffect(StatusEffects.SLOWNESS);
        player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
    }
}
