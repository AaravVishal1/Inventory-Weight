package com.inventoryweight.stamina;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.weight.WeightManager;
import com.inventoryweight.weight.WeightTier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StaminaManager {
    private static final Map<UUID, StaminaData> playerStamina = new ConcurrentHashMap<>();
    private static final float MAX_STAMINA = 100.0f;
    
    public static void updateStamina(PlayerEntity player) {
        WeightConfig config = WeightConfig.getInstance();
        if (!config.staminaSystemEnabled) {
            return;
        }
        
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        
        UUID playerId = player.getUuid();
        StaminaData data = playerStamina.computeIfAbsent(playerId, k -> new StaminaData());
        
        WeightTier tier = WeightManager.getPlayerTier(player);
        
        boolean isExerting = player.isSprinting() || player.isSneaking() || player.isSwimming();
        
        if (isExerting) {
            float drainRate = tier.getStaminaDrainRate();
            data.stamina = Math.max(0, data.stamina - drainRate);
        } else {
            float regenRate = tier.getStaminaRegenRate();
            if (!player.isSprinting()) {
                data.stamina = Math.min(MAX_STAMINA, data.stamina + regenRate);
            }
        }
        
        if (data.stamina <= 0 && player.isSprinting()) {
            player.setSprinting(false);
            data.exhausted = true;
        }
        
        if (data.exhausted && data.stamina >= config.staminaExhaustionRecoveryThreshold) {
            data.exhausted = false;
        }
    }
    
    public static float getStamina(PlayerEntity player) {
        StaminaData data = playerStamina.get(player.getUuid());
        return data != null ? data.stamina : MAX_STAMINA;
    }
    
    public static float getMaxStamina() {
        return MAX_STAMINA;
    }
    
    public static float getStaminaPercent(PlayerEntity player) {
        return getStamina(player) / MAX_STAMINA;
    }
    
    public static boolean isExhausted(PlayerEntity player) {
        StaminaData data = playerStamina.get(player.getUuid());
        return data != null && data.exhausted;
    }
    
    public static boolean canSprint(PlayerEntity player) {
        WeightConfig config = WeightConfig.getInstance();
        if (!config.staminaSystemEnabled) {
            return true;
        }
        
        StaminaData data = playerStamina.get(player.getUuid());
        if (data == null) {
            return true;
        }
        
        return !data.exhausted && data.stamina > 0;
    }
    
    public static void removePlayer(PlayerEntity player) {
        playerStamina.remove(player.getUuid());
    }
    
    private static class StaminaData {
        float stamina = MAX_STAMINA;
        boolean exhausted = false;
    }
}
