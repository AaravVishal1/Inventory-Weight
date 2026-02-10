package com.inventoryweight.dimension;

import com.inventoryweight.config.WeightConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public class DimensionWeightModifier {
    
    public static float getDimensionMultiplier(PlayerEntity player) {
        WeightConfig config = WeightConfig.getInstance();
        if (!config.dimensionModifiersEnabled) {
            return 1.0f;
        }
        
        RegistryKey<World> dimension = player.getWorld().getRegistryKey();
        
        if (dimension == World.NETHER) {
            return config.netherWeightMultiplier;
        } else if (dimension == World.END) {
            return config.endWeightMultiplier;
        } else if (dimension == World.OVERWORLD) {
            return config.overworldWeightMultiplier;
        }
        
        return 1.0f;
    }
    
    public static float getEnvironmentMultiplier(PlayerEntity player) {
        WeightConfig config = WeightConfig.getInstance();
        float multiplier = 1.0f;
        
        if (config.waterDepthAffectsWeight && player.isSubmergedInWater()) {
            double depth = getWaterDepth(player);
            multiplier *= 1.0f + (float)(depth * 0.01f);
        }
        
        if (config.rainAffectsWeight && player.getWorld().isRaining() && player.getWorld().isSkyVisible(player.getBlockPos())) {
            multiplier *= config.rainWeightMultiplier;
        }
        
        if (config.altitudeAffectsWeight) {
            double y = player.getY();
            if (y > 200) {
                float altitudeBonus = (float)((y - 200) / 100) * 0.1f;
                multiplier *= Math.max(0.8f, 1.0f - altitudeBonus);
            } else if (y < 0) {
                float pressurePenalty = (float)(Math.abs(y) / 64) * 0.1f;
                multiplier *= 1.0f + pressurePenalty;
            }
        }
        
        return multiplier;
    }
    
    private static double getWaterDepth(PlayerEntity player) {
        int depth = 0;
        var pos = player.getBlockPos();
        
        while (player.getWorld().getFluidState(pos.up(depth + 1)).isStill() && depth < 64) {
            depth++;
        }
        
        return depth;
    }
}
