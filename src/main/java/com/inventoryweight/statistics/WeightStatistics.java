package com.inventoryweight.statistics;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WeightStatistics {
    private static final Map<UUID, PlayerStats> playerStats = new ConcurrentHashMap<>();
    
    public static void recordWeight(PlayerEntity player, float weight) {
        UUID playerId = player.getUuid();
        PlayerStats stats = playerStats.computeIfAbsent(playerId, k -> new PlayerStats());
        
        stats.currentWeight = weight;
        stats.weightSamples++;
        stats.totalWeightSum += weight;
        
        if (weight > stats.peakWeight) {
            stats.peakWeight = weight;
        }
        
        if (weight < stats.lowestWeight || stats.lowestWeight == 0) {
            stats.lowestWeight = weight;
        }
    }
    
    public static void recordMovement(PlayerEntity player, double distance, boolean sprinting, boolean swimming, boolean climbing) {
        UUID playerId = player.getUuid();
        PlayerStats stats = playerStats.computeIfAbsent(playerId, k -> new PlayerStats());
        
        stats.totalDistance += distance;
        
        if (sprinting) {
            stats.sprintDistance += distance;
        }
        if (swimming) {
            stats.swimDistance += distance;
        }
        if (climbing) {
            stats.climbDistance += distance;
        }
    }
    
    public static void recordTierChange(PlayerEntity player, int tierOrdinal) {
        UUID playerId = player.getUuid();
        PlayerStats stats = playerStats.computeIfAbsent(playerId, k -> new PlayerStats());
        
        stats.tierChanges++;
        stats.tierTimeSpent[tierOrdinal]++;
    }
    
    public static void recordExhaustion(PlayerEntity player) {
        UUID playerId = player.getUuid();
        PlayerStats stats = playerStats.computeIfAbsent(playerId, k -> new PlayerStats());
        stats.timesExhausted++;
    }
    
    public static PlayerStats getStats(PlayerEntity player) {
        return playerStats.getOrDefault(player.getUuid(), new PlayerStats());
    }
    
    public static void removePlayer(UUID playerId) {
        playerStats.remove(playerId);
    }
    
    public static class PlayerStats {
        public float currentWeight = 0;
        public float peakWeight = 0;
        public float lowestWeight = 0;
        public long weightSamples = 0;
        public double totalWeightSum = 0;
        public double totalDistance = 0;
        public double sprintDistance = 0;
        public double swimDistance = 0;
        public double climbDistance = 0;
        public int tierChanges = 0;
        public int timesExhausted = 0;
        public long[] tierTimeSpent = new long[5];
        
        public float getAverageWeight() {
            if (weightSamples == 0) return 0;
            return (float)(totalWeightSum / weightSamples);
        }
    }
}
