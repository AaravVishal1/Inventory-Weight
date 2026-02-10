package com.inventoryweight.detection;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.weight.WeightManager;
import com.inventoryweight.weight.WeightTier;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.List;

public class MobDetectionHandler {
    
    public static void handleMobDetection(PlayerEntity player) {
        WeightConfig config = WeightConfig.getInstance();
        if (!config.enabled || !config.weightIncreasesDetection) {
            return;
        }
        
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        
        if (player.isSneaking()) {
            return;
        }
        
        if (!(player.getWorld() instanceof ServerWorld world)) {
            return;
        }
        
        WeightTier tier = WeightManager.getPlayerTier(player);
        double detectionRadius = tier.getDetectionRadiusBonus();
        
        if (detectionRadius <= 0) {
            return;
        }
        
        Box searchBox = player.getBoundingBox().expand(detectionRadius);
        List<HostileEntity> nearbyMobs = world.getEntitiesByClass(
                HostileEntity.class, 
                searchBox, 
                mob -> mob.getTarget() == null && mob.canSee(player)
        );
        
        for (HostileEntity mob : nearbyMobs) {
            double distance = mob.distanceTo(player);
            double normalDetection = 16.0;
            
            if (distance <= normalDetection + detectionRadius) {
                mob.setTarget(player);
            }
        }
    }
    
    public static float getFootstepVolumeMultiplier(PlayerEntity player) {
        WeightConfig config = WeightConfig.getInstance();
        if (!config.enabled || !config.weightIncreasesFootstepVolume) {
            return 1.0f;
        }
        
        if (player.isCreative() || player.isSpectator()) {
            return 1.0f;
        }
        
        WeightTier tier = WeightManager.getPlayerTier(player);
        return tier.getFootstepVolumeMultiplier();
    }
}
