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
public abstract class ElytraMixin {
    
    @Inject(method = "tickFallFlying", at = @At("HEAD"))
    private void modifyElytraFlight(CallbackInfo ci) {
        if (!((Object) this instanceof PlayerEntity player)) {
            return;
        }
        
        WeightConfig config = WeightConfig.getInstance();
        if (!config.enabled || !config.weightAffectsElytra) {
            return;
        }
        
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        
        if (!player.isFallFlying()) {
            return;
        }
        
        WeightTier tier = WeightManager.getPlayerTier(player);
        float sinkRate = tier.getElytraSinkRate();
        
        if (sinkRate > 0) {
            Vec3d velocity = player.getVelocity();
            player.setVelocity(velocity.x, velocity.y - sinkRate, velocity.z);
        }
    }
}
