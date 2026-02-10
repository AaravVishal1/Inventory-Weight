package com.inventoryweight.mixin;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.weight.WeightManager;
import com.inventoryweight.weight.WeightTier;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerMixin {
    
    @Shadow
    private float exhaustion;
    
    @Shadow
    public abstract void addExhaustion(float exhaustion);
    
    private int exhaustionTickCounter = 0;
    private PlayerEntity cachedPlayer = null;
    
    @Inject(method = "update", at = @At("HEAD"))
    private void onUpdate(PlayerEntity player, CallbackInfo ci) {
        this.cachedPlayer = player;
        
        WeightConfig config = WeightConfig.getInstance();
        if (!config.enabled || !config.weightCausesExhaustion) {
            return;
        }
        
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        
        exhaustionTickCounter++;
        if (exhaustionTickCounter < 20) {
            return;
        }
        exhaustionTickCounter = 0;
        
        WeightTier tier = WeightManager.getPlayerTier(player);
        float exhaustionAmount = tier.getExhaustionPerSecond();
        
        if (exhaustionAmount > 0) {
            addExhaustion(exhaustionAmount);
        }
    }
}
