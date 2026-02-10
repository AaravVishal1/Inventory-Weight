package com.inventoryweight.util;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.registry.ItemWeightRegistry;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class ShulkerBoxWeightCalculator {
    
    public static float calculateShulkerWeight(ItemStack shulkerStack) {
        if (!(shulkerStack.getItem() instanceof BlockItem blockItem)) {
            return 0.0f;
        }
        
        if (!(blockItem.getBlock() instanceof ShulkerBoxBlock)) {
            return 0.0f;
        }
        
        WeightConfig config = WeightConfig.getInstance();
        if (!config.shulkerBoxAddsContentWeight) {
            return ItemWeightRegistry.getWeight(shulkerStack.getItem());
        }
        
        float baseWeight = config.shulkerBoxBaseWeight;
        float contentWeight = 0.0f;
        
        NbtCompound nbt = shulkerStack.getNbt();
        if (nbt != null && nbt.contains("BlockEntityTag")) {
            NbtCompound blockEntityTag = nbt.getCompound("BlockEntityTag");
            if (blockEntityTag.contains("Items")) {
                DefaultedList<ItemStack> items = DefaultedList.ofSize(27, ItemStack.EMPTY);
                Inventories.readNbt(blockEntityTag, items);
                
                for (ItemStack stack : items) {
                    if (!stack.isEmpty()) {
                        float itemWeight = ItemWeightRegistry.getWeight(stack.getItem());
                        contentWeight += itemWeight * stack.getCount() * config.shulkerContentWeightMultiplier;
                    }
                }
            }
        }
        
        return baseWeight + contentWeight;
    }
    
    public static boolean isShulkerBox(ItemStack stack) {
        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            return false;
        }
        return blockItem.getBlock() instanceof ShulkerBoxBlock;
    }
}
