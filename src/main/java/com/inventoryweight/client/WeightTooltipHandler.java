package com.inventoryweight.client;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.registry.ItemWeightRegistry;
import com.inventoryweight.weight.WeightCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class WeightTooltipHandler {
    
    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (!WeightConfig.getInstance().showWeightInTooltip) {
                return;
            }
            
            Item item = stack.getItem();
            float weight;
            
            if (item instanceof ArmorItem) {
                weight = ItemWeightRegistry.getArmorWeight(item);
            } else {
                weight = ItemWeightRegistry.getWeight(item);
            }
            
            String category = getCategoryName(weight);
            Formatting color = getCategoryColor(weight);
            
            float stackWeight = weight * stack.getCount();
            
            if (stack.getCount() > 1) {
                lines.add(Text.literal(String.format("Weight: %.2f (%.2f each) [%s]", stackWeight, weight, category))
                        .formatted(color));
            } else {
                lines.add(Text.literal(String.format("Weight: %.2f [%s]", weight, category))
                        .formatted(color));
            }
        });
    }
    
    private static String getCategoryName(float weight) {
        if (weight >= WeightCategory.HEAVY.getBaseWeight()) {
            return "Heavy";
        } else if (weight >= WeightCategory.MEDIUM.getBaseWeight()) {
            return "Medium";
        } else if (weight >= WeightCategory.LIGHT.getBaseWeight()) {
            return "Light";
        } else {
            return "Near-Zero";
        }
    }
    
    private static Formatting getCategoryColor(float weight) {
        if (weight >= WeightCategory.HEAVY.getBaseWeight()) {
            return Formatting.RED;
        } else if (weight >= WeightCategory.MEDIUM.getBaseWeight()) {
            return Formatting.YELLOW;
        } else if (weight >= WeightCategory.LIGHT.getBaseWeight()) {
            return Formatting.GREEN;
        } else {
            return Formatting.GRAY;
        }
    }
}
