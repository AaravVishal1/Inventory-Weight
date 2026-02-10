package com.inventoryweight.registry;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.weight.WeightCategory;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemWeightRegistry {
    private static final Map<Item, Float> itemWeights = new HashMap<>();
    private static final Map<Item, Float> armorWeights = new HashMap<>();
    
    private static final Set<String> HEAVY_KEYWORDS = Set.of(
        "ore", "raw_", "ingot", "anvil", "stone", "deepslate", "cobblestone",
        "iron_block", "gold_block", "diamond_block", "netherite_block", "copper_block",
        "emerald_block", "lapis_block", "redstone_block", "obsidian", "crying_obsidian",
        "ancient_debris", "netherite_scrap", "netherite_ingot"
    );
    
    private static final Set<String> MEDIUM_KEYWORDS = Set.of(
        "sword", "pickaxe", "axe", "shovel", "hoe", "bow", "crossbow", "trident",
        "shield", "redstone", "piston", "hopper", "dropper", "dispenser", "observer",
        "comparator", "repeater", "planks", "log", "wood", "brick", "terracotta",
        "concrete", "furnace", "blast_furnace", "smoker", "crafting_table", "anvil",
        "cauldron", "bell", "chain", "lantern", "campfire", "barrel", "chest",
        "shulker_box", "ender_chest", "beacon", "conduit", "lodestone"
    );
    
    private static final Set<String> LIGHT_KEYWORDS = Set.of(
        "apple", "bread", "beef", "pork", "chicken", "mutton", "rabbit", "cod", "salmon",
        "cookie", "cake", "pie", "carrot", "potato", "beetroot", "melon", "berry",
        "wheat", "seeds", "sapling", "flower", "grass", "fern", "vine", "mushroom",
        "sugar", "cocoa", "egg", "honey", "wool", "string", "feather", "leather",
        "paper", "bamboo", "kelp", "seagrass", "lily_pad", "moss", "azalea", "dripleaf",
        "glow_berries", "sweet_berries", "chorus_fruit", "golden_apple", "enchanted_golden_apple",
        "rabbit_foot", "rabbit_hide", "spider_eye", "rotten_flesh", "bone", "slime_ball"
    );
    
    private static final Set<String> NEAR_ZERO_KEYWORDS = Set.of(
        "map", "filled_map", "book", "writable_book", "written_book", "enchanted_book",
        "potion", "splash_potion", "lingering_potion", "ender_pearl", "ender_eye",
        "experience_bottle", "name_tag", "lead", "music_disc", "echo_shard",
        "amethyst_shard", "prismarine_shard", "nether_star", "heart_of_the_sea",
        "totem_of_undying", "elytra", "firework_rocket", "firework_star",
        "knowledge_book", "debug_stick", "bundle"
    );
    
    private static final Set<String> HEAVY_ARMOR_KEYWORDS = Set.of(
        "netherite", "diamond", "iron", "golden", "chainmail"
    );
    
    private static final Set<String> LIGHT_ARMOR_KEYWORDS = Set.of(
        "leather", "turtle"
    );
    
    public static void initialize() {
        for (Item item : Registries.ITEM) {
            Identifier id = Registries.ITEM.getId(item);
            String path = id.getPath();
            
            if (item instanceof ArmorItem armorItem) {
                float armorWeight = calculateArmorWeight(armorItem, path);
                armorWeights.put(item, armorWeight);
                itemWeights.put(item, armorWeight * 0.5f);
            } else {
                float weight = calculateItemWeight(item, path);
                itemWeights.put(item, weight);
            }
        }
    }
    
    private static float calculateItemWeight(Item item, String path) {
        for (String keyword : NEAR_ZERO_KEYWORDS) {
            if (path.contains(keyword)) {
                return WeightCategory.NEAR_ZERO.getBaseWeight();
            }
        }
        
        for (String keyword : LIGHT_KEYWORDS) {
            if (path.contains(keyword)) {
                return WeightCategory.LIGHT.getBaseWeight();
            }
        }
        
        for (String keyword : HEAVY_KEYWORDS) {
            if (path.contains(keyword)) {
                return WeightCategory.HEAVY.getBaseWeight();
            }
        }
        
        for (String keyword : MEDIUM_KEYWORDS) {
            if (path.contains(keyword)) {
                return WeightCategory.MEDIUM.getBaseWeight();
            }
        }
        
        if (item instanceof ToolItem) {
            return WeightCategory.MEDIUM.getBaseWeight();
        }
        
        if (item instanceof BlockItem) {
            return WeightCategory.MEDIUM.getBaseWeight();
        }
        
        return WeightCategory.DEFAULT.getBaseWeight();
    }
    
    private static float calculateArmorWeight(ArmorItem armorItem, String path) {
        float baseWeight;
        
        boolean isHeavy = false;
        for (String keyword : HEAVY_ARMOR_KEYWORDS) {
            if (path.startsWith(keyword)) {
                isHeavy = true;
                break;
            }
        }
        
        boolean isLight = false;
        for (String keyword : LIGHT_ARMOR_KEYWORDS) {
            if (path.startsWith(keyword)) {
                isLight = true;
                break;
            }
        }
        
        if (isHeavy) {
            baseWeight = 8.0f;
        } else if (isLight) {
            baseWeight = 2.0f;
        } else {
            baseWeight = 5.0f;
        }
        
        float slotMultiplier = switch (armorItem.getSlotType()) {
            case HEAD -> 0.6f;
            case CHEST -> 1.0f;
            case LEGS -> 0.8f;
            case FEET -> 0.4f;
            default -> 0.5f;
        };
        
        if (path.startsWith("netherite")) {
            baseWeight *= 1.5f;
        } else if (path.startsWith("diamond")) {
            baseWeight *= 1.2f;
        }
        
        return baseWeight * slotMultiplier;
    }
    
    public static float getWeight(Item item) {
        WeightConfig config = WeightConfig.getInstance();
        Identifier id = Registries.ITEM.getId(item);
        String itemId = id.toString();
        
        if (config.customItemWeights.containsKey(itemId)) {
            return config.customItemWeights.get(itemId);
        }
        
        return itemWeights.getOrDefault(item, WeightCategory.DEFAULT.getBaseWeight());
    }
    
    public static float getArmorWeight(Item item) {
        WeightConfig config = WeightConfig.getInstance();
        Identifier id = Registries.ITEM.getId(item);
        String itemId = id.toString();
        
        if (config.customItemWeights.containsKey(itemId)) {
            return config.customItemWeights.get(itemId);
        }
        
        return armorWeights.getOrDefault(item, WeightCategory.MEDIUM.getBaseWeight());
    }
    
    public static void setCustomWeight(Item item, float weight) {
        itemWeights.put(item, weight);
    }
    
    public static void setCustomArmorWeight(Item item, float weight) {
        armorWeights.put(item, weight);
    }
}
