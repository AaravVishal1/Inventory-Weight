package com.inventoryweight.advancement;

import com.inventoryweight.InventoryWeight;
import com.inventoryweight.weight.WeightTier;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WeightAdvancements {
    private static final Map<UUID, PlayerAdvancementData> playerData = new HashMap<>();
    
    public static final String ADVANCEMENT_LIGHT_TRAVELER = "inventoryweight:light_traveler";
    public static final String ADVANCEMENT_PACK_MULE = "inventoryweight:pack_mule";
    public static final String ADVANCEMENT_BEAST_OF_BURDEN = "inventoryweight:beast_of_burden";
    public static final String ADVANCEMENT_IMMOVABLE_OBJECT = "inventoryweight:immovable_object";
    public static final String ADVANCEMENT_MARATHON_RUNNER = "inventoryweight:marathon_runner";
    public static final String ADVANCEMENT_DEEP_DIVER = "inventoryweight:deep_diver";
    public static final String ADVANCEMENT_MOUNTAIN_CLIMBER = "inventoryweight:mountain_climber";
    
    public static void onTierReached(ServerPlayerEntity player, WeightTier tier) {
        switch (tier) {
            case HEAVY -> grantAdvancement(player, ADVANCEMENT_PACK_MULE);
            case OVERLOADED -> grantAdvancement(player, ADVANCEMENT_BEAST_OF_BURDEN);
            case CRIPPLED -> grantAdvancement(player, ADVANCEMENT_IMMOVABLE_OBJECT);
            default -> {}
        }
    }
    
    public static void onDistanceTraveled(ServerPlayerEntity player, float weight, double distance) {
        UUID playerId = player.getUuid();
        PlayerAdvancementData data = playerData.computeIfAbsent(playerId, k -> new PlayerAdvancementData());
        
        if (weight < 10.0f) {
            data.lightTravelDistance += distance;
            if (data.lightTravelDistance >= 10000) {
                grantAdvancement(player, ADVANCEMENT_LIGHT_TRAVELER);
            }
        }
        
        if (weight >= 100.0f) {
            data.heavyTravelDistance += distance;
            if (data.heavyTravelDistance >= 5000) {
                grantAdvancement(player, ADVANCEMENT_MARATHON_RUNNER);
            }
        }
    }
    
    public static void onSwimWhileHeavy(ServerPlayerEntity player, float weight, double depth) {
        if (weight >= 150.0f && depth >= 20) {
            grantAdvancement(player, ADVANCEMENT_DEEP_DIVER);
        }
    }
    
    public static void onClimbWhileHeavy(ServerPlayerEntity player, float weight, double height) {
        UUID playerId = player.getUuid();
        PlayerAdvancementData data = playerData.computeIfAbsent(playerId, k -> new PlayerAdvancementData());
        
        if (weight >= 150.0f) {
            data.climbHeight += height;
            if (data.climbHeight >= 100) {
                grantAdvancement(player, ADVANCEMENT_MOUNTAIN_CLIMBER);
            }
        }
    }
    
    private static void grantAdvancement(ServerPlayerEntity player, String advancementId) {
        var advancementLoader = player.server.getAdvancementLoader();
        Advancement advancement = advancementLoader.get(new Identifier(advancementId));
        
        if (advancement != null) {
            AdvancementProgress progress = player.getAdvancementTracker().getProgress(advancement);
            if (!progress.isDone()) {
                for (String criterion : progress.getUnobtainedCriteria()) {
                    player.getAdvancementTracker().grantCriterion(advancement, criterion);
                }
            }
        }
    }
    
    public static void removePlayer(UUID playerId) {
        playerData.remove(playerId);
    }
    
    private static class PlayerAdvancementData {
        double lightTravelDistance = 0;
        double heavyTravelDistance = 0;
        double climbHeight = 0;
    }
}
