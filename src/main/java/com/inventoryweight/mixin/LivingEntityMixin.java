package com.inventoryweight.mixin;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.util.TransportHelper;
import com.inventoryweight.weight.WeightManager;
import com.inventoryweight.weight.WeightTier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    
    @Inject(method = "getJumpVelocity", at = @At("RETURN"), cancellable = true)
    private void modifyJumpVelocity(CallbackInfoReturnable<Float> cir) {
        if (!((Object) this instanceof PlayerEntity player)) {
            return;
        }
        
        if (!WeightConfig.getInstance().enabled) {
            return;
        }
        
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        
        if (TransportHelper.isOnTransport(player)) {
            return;
        }
        
        WeightTier tier = WeightManager.getPlayerTier(player);
        float multiplier = tier.getJumpMultiplier();
        
        if (multiplier < 1.0f) {
            cir.setReturnValue(cir.getReturnValue() * multiplier);
        }
    }
    
    @ModifyVariable(method = "takeKnockback", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private double modifyKnockbackStrength(double strength) {
        if (!((Object) this instanceof PlayerEntity player)) {
            return strength;
        }
        
        if (!WeightConfig.getInstance().enabled) {
            return strength;
        }
        
        if (player.isCreative() || player.isSpectator()) {
            return strength;
        }
        
        WeightTier tier = WeightManager.getPlayerTier(player);
        float knockbackModifier = tier.getKnockbackResistanceModifier();
        
        if (knockbackModifier < 0) {
            return strength * (1.0 - knockbackModifier);
        }
        
        return strength;
    }
}
