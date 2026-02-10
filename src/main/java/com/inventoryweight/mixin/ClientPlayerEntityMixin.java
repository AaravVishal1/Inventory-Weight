package com.inventoryweight.mixin;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.util.TransportHelper;
import com.inventoryweight.weight.WeightManager;
import com.inventoryweight.weight.WeightTier;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    
    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void onTickMovement(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        
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
