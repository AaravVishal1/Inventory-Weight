package com.inventoryweight.mixin;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.weight.WeightManager;
import com.inventoryweight.weight.WeightTier;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class BlockBreakMixin {
    
    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    private void modifyBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        
        WeightConfig config = WeightConfig.getInstance();
        if (!config.enabled || !config.weightAffectsMining) {
            return;
        }
        
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        
        WeightTier tier = WeightManager.getPlayerTier(player);
        float multiplier = tier.getMiningSpeedMultiplier();
        
        if (multiplier < 1.0f) {
            cir.setReturnValue(cir.getReturnValue() * multiplier);
        }
    }
}
