package com.inventoryweight.weight;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.dimension.DimensionWeightModifier;
import com.inventoryweight.effect.TierChangeEffects;
import com.inventoryweight.registry.ItemWeightRegistry;
import com.inventoryweight.util.ShulkerBoxWeightCalculator;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WeightManager {
    private static final Map<UUID, PlayerWeightData> playerWeights = new ConcurrentHashMap<>();
    private static final int UPDATE_INTERVAL_TICKS = 10;
    
    public static void updatePlayerWeight(PlayerEntity player) {
        UUID playerId = player.getUuid();
        PlayerWeightData data = playerWeights.computeIfAbsent(playerId, k -> new PlayerWeightData());
        
        if (data.ticksSinceUpdate < UPDATE_INTERVAL_TICKS) {
            data.ticksSinceUpdate++;
            return;
        }
        
        data.ticksSinceUpdate = 0;
        float newWeight = calculateTotalWeight(player);
        
        if (Math.abs(newWeight - data.currentWeight) > 0.01f) {
            WeightTier oldTier = data.currentTier;
            data.currentWeight = newWeight;
            WeightTier newTier = WeightTier.fromWeight(newWeight);
            data.currentTier = newTier;
            
            if (oldTier != newTier) {
                TierChangeEffects.onTierChanged(player, oldTier, newTier);
            }
        }
    }
    
    public static float calculateTotalWeight(PlayerEntity player) {
        WeightConfig config = WeightConfig.getInstance();
        float totalWeight = 0.0f;
        
        PlayerInventory inventory = player.getInventory();
        
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                float itemWeight = getStackWeight(stack);
                totalWeight += itemWeight * config.hotbarWeightMultiplier;
            }
        }
        
        for (int i = 9; i < 36; i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                totalWeight += getStackWeight(stack);
            }
        }
        
        for (EquipmentSlot slot : new EquipmentSlot[]{
                EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack armorStack = player.getEquippedStack(slot);
            if (!armorStack.isEmpty()) {
                float armorWeight = ItemWeightRegistry.getArmorWeight(armorStack.getItem());
                totalWeight += armorWeight;
            }
        }
        
        ItemStack offhandStack = player.getOffHandStack();
        if (!offhandStack.isEmpty()) {
            float itemWeight = getStackWeight(offhandStack);
            totalWeight += itemWeight * config.hotbarWeightMultiplier;
        }
        
        if (config.hardcoreMode) {
            totalWeight *= config.hardcoreMultiplier;
        }
        
        totalWeight *= DimensionWeightModifier.getDimensionMultiplier(player);
        totalWeight *= DimensionWeightModifier.getEnvironmentMultiplier(player);
        
        return totalWeight;
    }
    
    private static float getStackWeight(ItemStack stack) {
        if (ShulkerBoxWeightCalculator.isShulkerBox(stack)) {
            return ShulkerBoxWeightCalculator.calculateShulkerWeight(stack);
        }
        return ItemWeightRegistry.getWeight(stack.getItem()) * stack.getCount();
    }
    
    public static float getPlayerWeight(PlayerEntity player) {
        PlayerWeightData data = playerWeights.get(player.getUuid());
        return data != null ? data.currentWeight : 0.0f;
    }
    
    public static WeightTier getPlayerTier(PlayerEntity player) {
        PlayerWeightData data = playerWeights.get(player.getUuid());
        return data != null ? data.currentTier : WeightTier.LIGHT;
    }
    
    public static void removePlayer(PlayerEntity player) {
        playerWeights.remove(player.getUuid());
    }
    
    public static void forceUpdate(PlayerEntity player) {
        PlayerWeightData data = playerWeights.computeIfAbsent(player.getUuid(), k -> new PlayerWeightData());
        data.ticksSinceUpdate = UPDATE_INTERVAL_TICKS;
        updatePlayerWeight(player);
    }
    
    private static class PlayerWeightData {
        float currentWeight = 0.0f;
        WeightTier currentTier = WeightTier.LIGHT;
        int ticksSinceUpdate = UPDATE_INTERVAL_TICKS;
    }
}
