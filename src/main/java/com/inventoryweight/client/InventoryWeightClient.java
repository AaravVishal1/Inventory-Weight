package com.inventoryweight.client;

import com.inventoryweight.stamina.StaminaManager;
import com.inventoryweight.weight.WeightManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class InventoryWeightClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        WeightTooltipHandler.register();
        
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && !client.player.isCreative() && !client.player.isSpectator()) {
                WeightHudRenderer.render(drawContext, client.player);
                StaminaBarRenderer.render(drawContext, client.player, 
                        client.getWindow().getScaledWidth(), 
                        client.getWindow().getScaledHeight());
            }
        });
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && !client.player.isCreative() && !client.player.isSpectator()) {
                WeightManager.updatePlayerWeight(client.player);
                StaminaManager.updateStamina(client.player);
            }
        });
    }
}
