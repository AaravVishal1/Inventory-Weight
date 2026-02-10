package com.inventoryweight.command;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.registry.ItemWeightRegistry;
import com.inventoryweight.stamina.StaminaManager;
import com.inventoryweight.statistics.WeightStatistics;
import com.inventoryweight.weight.WeightManager;
import com.inventoryweight.weight.WeightTier;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class WeightCommands {
    
    public static void register() {
        CommandRegistrationCallback.EVENT.register(WeightCommands::registerCommands);
    }
    
    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher,
                                         CommandRegistryAccess registryAccess,
                                         CommandManager.RegistrationEnvironment environment) {
        
        dispatcher.register(CommandManager.literal("weight")
                .then(CommandManager.literal("check")
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                            float weight = WeightManager.getPlayerWeight(player);
                            WeightTier tier = WeightManager.getPlayerTier(player);
                            float stamina = StaminaManager.getStamina(player);
                            
                            context.getSource().sendFeedback(() ->
                                    Text.literal("Current Weight: ")
                                            .formatted(Formatting.GOLD)
                                            .append(Text.literal(String.format("%.1f", weight))
                                                    .formatted(Formatting.WHITE))
                                            .append(Text.literal(" | Tier: ")
                                                    .formatted(Formatting.GOLD))
                                            .append(Text.literal(tier.getDisplayName())
                                                    .styled(style -> style.withColor(tier.getColor())))
                                            .append(Text.literal(" | Stamina: ")
                                                    .formatted(Formatting.GOLD))
                                            .append(Text.literal(String.format("%.0f%%", stamina))
                                                    .formatted(Formatting.AQUA)),
                                    false);
                            return 1;
                        }))
                
                .then(CommandManager.literal("reload")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> {
                            WeightConfig.load();
                            ItemWeightRegistry.initialize();
                            context.getSource().sendFeedback(() ->
                                    Text.literal("Inventory Weight config reloaded!")
                                            .formatted(Formatting.GREEN), true);
                            return 1;
                        }))
                
                .then(CommandManager.literal("set")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("item", StringArgumentType.string())
                                .then(CommandManager.argument("weight", FloatArgumentType.floatArg(0.0f, 100.0f))
                                        .executes(context -> {
                                            String itemId = StringArgumentType.getString(context, "item");
                                            float weight = FloatArgumentType.getFloat(context, "weight");
                                            
                                            Identifier id = new Identifier(itemId);
                                            if (!Registries.ITEM.containsId(id)) {
                                                context.getSource().sendError(
                                                        Text.literal("Unknown item: " + itemId));
                                                return 0;
                                            }
                                            
                                            Item item = Registries.ITEM.get(id);
                                            WeightConfig.getInstance().customItemWeights.put(itemId, weight);
                                            WeightConfig.save();
                                            ItemWeightRegistry.setCustomWeight(item, weight);
                                            
                                            context.getSource().sendFeedback(() ->
                                                    Text.literal("Set weight of ")
                                                            .formatted(Formatting.GREEN)
                                                            .append(Text.literal(itemId)
                                                                    .formatted(Formatting.YELLOW))
                                                            .append(Text.literal(" to ")
                                                                    .formatted(Formatting.GREEN))
                                                            .append(Text.literal(String.format("%.2f", weight))
                                                                    .formatted(Formatting.WHITE)),
                                                    true);
                                            return 1;
                                        }))))
                
                .then(CommandManager.literal("thresholds")
                        .executes(context -> {
                            WeightConfig config = WeightConfig.getInstance();
                            context.getSource().sendFeedback(() ->
                                    Text.literal("Weight Thresholds:\n")
                                            .formatted(Formatting.GOLD)
                                            .append(Text.literal("  Light: 0 - " + config.mediumTierThreshold + "\n")
                                                    .formatted(Formatting.WHITE))
                                            .append(Text.literal("  Medium: " + config.mediumTierThreshold + " - " + config.heavyTierThreshold + "\n")
                                                    .formatted(Formatting.YELLOW))
                                            .append(Text.literal("  Heavy: " + config.heavyTierThreshold + " - " + config.overloadedTierThreshold + "\n")
                                                    .formatted(Formatting.GOLD))
                                            .append(Text.literal("  Overloaded: " + config.overloadedTierThreshold + " - " + config.crippledTierThreshold + "\n")
                                                    .styled(style -> style.withColor(0xFF6600)))
                                            .append(Text.literal("  Crippled: " + config.crippledTierThreshold + "+")
                                                    .formatted(Formatting.RED)),
                                    false);
                            return 1;
                        }))
                
                .then(CommandManager.literal("toggle")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> {
                            WeightConfig config = WeightConfig.getInstance();
                            config.enabled = !config.enabled;
                            WeightConfig.save();
                            
                            String status = config.enabled ? "enabled" : "disabled";
                            Formatting color = config.enabled ? Formatting.GREEN : Formatting.RED;
                            
                            context.getSource().sendFeedback(() ->
                                    Text.literal("Inventory Weight system ")
                                            .formatted(Formatting.GOLD)
                                            .append(Text.literal(status)
                                                    .formatted(color)),
                                    true);
                            return 1;
                        }))
                
                .then(CommandManager.literal("hardcore")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> {
                            WeightConfig config = WeightConfig.getInstance();
                            config.hardcoreMode = !config.hardcoreMode;
                            WeightConfig.save();
                            
                            String status = config.hardcoreMode ? "enabled" : "disabled";
                            Formatting color = config.hardcoreMode ? Formatting.RED : Formatting.GREEN;
                            
                            context.getSource().sendFeedback(() ->
                                    Text.literal("Hardcore mode ")
                                            .formatted(Formatting.GOLD)
                                            .append(Text.literal(status)
                                                    .formatted(color)),
                                    true);
                            return 1;
                        }))
                
                .then(CommandManager.literal("stats")
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                            WeightStatistics.PlayerStats stats = WeightStatistics.getStats(player);
                            
                            context.getSource().sendFeedback(() ->
                                    Text.literal("=== Weight Statistics ===\n")
                                            .formatted(Formatting.GOLD)
                                            .append(Text.literal("Peak Weight: ")
                                                    .formatted(Formatting.GRAY))
                                            .append(Text.literal(String.format("%.1f\n", stats.peakWeight))
                                                    .formatted(Formatting.WHITE))
                                            .append(Text.literal("Average Weight: ")
                                                    .formatted(Formatting.GRAY))
                                            .append(Text.literal(String.format("%.1f\n", stats.getAverageWeight()))
                                                    .formatted(Formatting.WHITE))
                                            .append(Text.literal("Distance Traveled: ")
                                                    .formatted(Formatting.GRAY))
                                            .append(Text.literal(String.format("%.0f blocks\n", stats.totalDistance))
                                                    .formatted(Formatting.WHITE))
                                            .append(Text.literal("Sprint Distance: ")
                                                    .formatted(Formatting.GRAY))
                                            .append(Text.literal(String.format("%.0f blocks\n", stats.sprintDistance))
                                                    .formatted(Formatting.WHITE))
                                            .append(Text.literal("Tier Changes: ")
                                                    .formatted(Formatting.GRAY))
                                            .append(Text.literal(String.format("%d\n", stats.tierChanges))
                                                    .formatted(Formatting.WHITE))
                                            .append(Text.literal("Times Exhausted: ")
                                                    .formatted(Formatting.GRAY))
                                            .append(Text.literal(String.format("%d", stats.timesExhausted))
                                                    .formatted(Formatting.WHITE)),
                                    false);
                            return 1;
                        }))
                
                .then(CommandManager.literal("stamina")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> {
                            WeightConfig config = WeightConfig.getInstance();
                            config.staminaSystemEnabled = !config.staminaSystemEnabled;
                            WeightConfig.save();
                            
                            String status = config.staminaSystemEnabled ? "enabled" : "disabled";
                            Formatting color = config.staminaSystemEnabled ? Formatting.GREEN : Formatting.RED;
                            
                            context.getSource().sendFeedback(() ->
                                    Text.literal("Stamina system ")
                                            .formatted(Formatting.GOLD)
                                            .append(Text.literal(status)
                                                    .formatted(color)),
                                    true);
                            return 1;
                        }))
                
                .then(CommandManager.literal("dimension")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> {
                            WeightConfig config = WeightConfig.getInstance();
                            config.dimensionModifiersEnabled = !config.dimensionModifiersEnabled;
                            WeightConfig.save();
                            
                            String status = config.dimensionModifiersEnabled ? "enabled" : "disabled";
                            Formatting color = config.dimensionModifiersEnabled ? Formatting.GREEN : Formatting.RED;
                            
                            context.getSource().sendFeedback(() ->
                                    Text.literal("Dimension modifiers ")
                                            .formatted(Formatting.GOLD)
                                            .append(Text.literal(status)
                                                    .formatted(color)),
                                    true);
                            return 1;
                        }))
                
                .then(CommandManager.literal("effects")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> {
                            WeightConfig config = WeightConfig.getInstance();
                            config.applyStatusEffects = !config.applyStatusEffects;
                            WeightConfig.save();
                            
                            String status = config.applyStatusEffects ? "enabled" : "disabled";
                            Formatting color = config.applyStatusEffects ? Formatting.GREEN : Formatting.RED;
                            
                            context.getSource().sendFeedback(() ->
                                    Text.literal("Status effects ")
                                            .formatted(Formatting.GOLD)
                                            .append(Text.literal(status)
                                                    .formatted(color)),
                                    true);
                            return 1;
                        }))
        );
    }
}
