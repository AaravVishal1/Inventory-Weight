package com.inventoryweight.mixin;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.weight.WeightManager;
import com.inventoryweight.weight.WeightTier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ClimbingMixin {
    
    @Inject(method = "travel", at = @At("HEAD"))
    private void modifyClimbingSpeed(Vec3d movementInput, CallbackInfo ci) {
        if (!((Object) this instanceof PlayerEntity player)) {
            return;
        }
        
        if (!player.isClimbing()) {
            return;
        }
        
        WeightConfig config = WeightConfig.getInstance();
        if (!config.enabled || !config.weightAffectsClimbing) {
            return;
        }
        
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        
        WeightTier tier = WeightManager.getPlayerTier(player);
        float multiplier = tier.getClimbingMultiplier();
        
        if (multiplier < 1.0f) {
            Vec3d velocity = player.getVelocity();
            double climbY = velocity.y;
            if (climbY > 0) {
                player.setVelocity(velocity.x, climbY * multiplier, velocity.z);
            }
        }
    }
}
