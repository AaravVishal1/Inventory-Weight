package com.inventoryweight.mixin;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.util.TransportHelper;
import com.inventoryweight.weight.WeightManager;
import com.inventoryweight.weight.WeightTier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class FallDamageMixin {
    
    @ModifyVariable(method = "handleFallDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float modifyFallDistance(float fallDistance) {
        if (!((Object) this instanceof PlayerEntity player)) {
            return fallDistance;
        }
        
        WeightConfig config = WeightConfig.getInstance();
        if (!config.enabled || !config.weightAffectsFallDamage) {
            return fallDistance;
        }
        
        if (player.isCreative() || player.isSpectator()) {
            return fallDistance;
        }
        
        WeightTier tier = WeightManager.getPlayerTier(player);
        float multiplier = tier.getFallDamageMultiplier();
        
        return fallDistance * multiplier;
    }
}
