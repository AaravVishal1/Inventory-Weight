package com.inventoryweight.client;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.weight.WeightManager;
import com.inventoryweight.weight.WeightTier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class WeightHudRenderer {
    
    public static void render(DrawContext context, PlayerEntity player) {
        if (!WeightConfig.getInstance().showHud) {
            return;
        }
        
        float currentWeight = WeightManager.getPlayerWeight(player);
        WeightTier tier = WeightManager.getPlayerTier(player);
        
        String weightText = String.format("Weight: %.1f", currentWeight);
        String tierText = tier.getDisplayName();
        
        int color = tier.getColor();
        
        int x = 5;
        int y = 5;
        
        context.drawTextWithShadow(
            net.minecraft.client.MinecraftClient.getInstance().textRenderer,
            Text.literal(weightText),
            x, y, color
        );
        
        context.drawTextWithShadow(
            net.minecraft.client.MinecraftClient.getInstance().textRenderer,
            Text.literal(tierText),
            x, y + 12, color
        );
    }
}
