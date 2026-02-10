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
public abstract class SwimmingMixin {
    
    @Inject(method = "swimUpward", at = @At("HEAD"), cancellable = true)
    private void modifySwimUpward(CallbackInfo ci) {
        if (!((Object) this instanceof PlayerEntity player)) {
            return;
        }
        
        WeightConfig config = WeightConfig.getInstance();
        if (!config.enabled || !config.weightAffectsSwimming) {
            return;
        }
        
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        
        WeightTier tier = WeightManager.getPlayerTier(player);
        float multiplier = tier.getSwimmingMultiplier();
        
        if (multiplier < 1.0f) {
            Vec3d velocity = player.getVelocity();
            double upwardSpeed = 0.04 * multiplier;
            player.setVelocity(velocity.x, velocity.y + upwardSpeed, velocity.z);
            ci.cancel();
        }
    }
}
