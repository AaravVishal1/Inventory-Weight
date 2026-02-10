package com.inventoryweight.client;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.stamina.StaminaManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;

@Environment(EnvType.CLIENT)
public class StaminaBarRenderer {
    
    private static final int BAR_WIDTH = 80;
    private static final int BAR_HEIGHT = 5;
    
    public static void render(DrawContext context, PlayerEntity player, int screenWidth, int screenHeight) {
        WeightConfig config = WeightConfig.getInstance();
        if (!config.staminaSystemEnabled || !config.showStaminaBar) {
            return;
        }
        
        float staminaPercent = StaminaManager.getStaminaPercent(player);
        boolean exhausted = StaminaManager.isExhausted(player);
        
        int x = screenWidth / 2 - BAR_WIDTH / 2;
        int y = screenHeight - 50;
        
        context.fill(x - 1, y - 1, x + BAR_WIDTH + 1, y + BAR_HEIGHT + 1, 0xFF000000);
        
        context.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0xFF333333);
        
        int filledWidth = (int)(BAR_WIDTH * staminaPercent);
        int color = getStaminaColor(staminaPercent, exhausted);
        context.fill(x, y, x + filledWidth, y + BAR_HEIGHT, color);
    }
    
    private static int getStaminaColor(float percent, boolean exhausted) {
        if (exhausted) {
            return 0xFFFF0000;
        }
        
        if (percent > 0.6f) {
            return 0xFF00FF00;
        } else if (percent > 0.3f) {
            return 0xFFFFFF00;
        } else {
            return 0xFFFF6600;
        }
    }
}
