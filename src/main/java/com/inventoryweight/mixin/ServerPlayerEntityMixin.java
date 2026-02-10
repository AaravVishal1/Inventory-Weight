package com.inventoryweight.mixin;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.util.TransportHelper;
import com.inventoryweight.weight.WeightManager;
import com.inventoryweight.weight.WeightTier;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        
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
        
        if (!tier.canSprint() && player.isSprinting()) {
            player.setSprinting(false);
        }
    }
}
