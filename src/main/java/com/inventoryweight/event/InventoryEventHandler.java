package com.inventoryweight.event;

import com.inventoryweight.weight.WeightManager;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public class InventoryEventHandler {
    
    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (!world.isClient()) {
                WeightManager.forceUpdate(player);
            }
        });
        
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!world.isClient()) {
                WeightManager.forceUpdate(player);
            }
            return TypedActionResult.pass(player.getStackInHand(hand));
        });
        
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClient()) {
                WeightManager.forceUpdate(player);
            }
            return ActionResult.PASS;
        });
    }
}
