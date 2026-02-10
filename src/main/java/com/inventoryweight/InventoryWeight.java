package com.inventoryweight;

import com.inventoryweight.command.WeightCommands;
import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.detection.MobDetectionHandler;
import com.inventoryweight.effect.WeightEffects;
import com.inventoryweight.event.InventoryEventHandler;
import com.inventoryweight.registry.ItemWeightRegistry;
import com.inventoryweight.stamina.StaminaManager;
import com.inventoryweight.statistics.WeightStatistics;
import com.inventoryweight.weight.WeightManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryWeight implements ModInitializer {
    public static final String MOD_ID = "inventoryweight";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Inventory Weight mod");
        
        WeightConfig.load();
        ItemWeightRegistry.initialize();
        InventoryEventHandler.register();
        WeightCommands.register();
        
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerManager().getPlayerList().forEach(player -> {
                if (!player.isCreative() && !player.isSpectator()) {
                    WeightManager.updatePlayerWeight(player);
                    StaminaManager.updateStamina(player);
                    WeightEffects.applyTierEffects(player);
                    
                    if (server.getTicks() % 20 == 0) {
                        MobDetectionHandler.handleMobDetection(player);
                        WeightStatistics.recordWeight(player, WeightManager.getPlayerWeight(player));
                    }
                }
            });
        });
        
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            WeightManager.removePlayer(handler.getPlayer());
            StaminaManager.removePlayer(handler.getPlayer());
            WeightStatistics.removePlayer(handler.getPlayer().getUuid());
        });
        
        LOGGER.info("Inventory Weight mod initialized");
    }
}
