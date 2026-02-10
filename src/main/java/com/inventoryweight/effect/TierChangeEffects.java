package com.inventoryweight.effect;

import com.inventoryweight.config.WeightConfig;
import com.inventoryweight.weight.WeightTier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TierChangeEffects {
    
    public static void onTierChanged(PlayerEntity player, WeightTier oldTier, WeightTier newTier) {
        WeightConfig config = WeightConfig.getInstance();
        
        if (config.playTierChangeSounds) {
            playTierChangeSound(player, oldTier, newTier);
        }
        
        if (config.showTierChangeParticles && player instanceof ServerPlayerEntity serverPlayer) {
            spawnTierChangeParticles(serverPlayer, newTier);
        }
        
        sendTierChangeMessage(player, oldTier, newTier);
    }
    
    private static void playTierChangeSound(PlayerEntity player, WeightTier oldTier, WeightTier newTier) {
        boolean gettingHeavier = newTier.ordinalValue() > oldTier.ordinalValue();
        
        if (gettingHeavier) {
            switch (newTier) {
                case MEDIUM -> player.playSound(SoundEvents.ENTITY_ARMOR_STAND_FALL, SoundCategory.PLAYERS, 0.5f, 0.8f);
                case HEAVY -> player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 0.6f, 0.7f);
                case OVERLOADED -> player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.PLAYERS, 0.7f, 0.5f);
                case CRIPPLED -> player.playSound(SoundEvents.ENTITY_IRON_GOLEM_HURT, SoundCategory.PLAYERS, 0.6f, 0.4f);
                default -> {}
            }
        } else {
            switch (newTier) {
                case LIGHT -> player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.3f, 1.5f);
                case MEDIUM -> player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.4f, 1.2f);
                case HEAVY -> player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.3f, 1.0f);
                case OVERLOADED -> player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.3f, 0.8f);
                default -> {}
            }
        }
    }
    
    private static void spawnTierChangeParticles(ServerPlayerEntity player, WeightTier newTier) {
        ServerWorld world = player.getServerWorld();
        double x = player.getX();
        double y = player.getY() + 1.0;
        double z = player.getZ();
        
        switch (newTier) {
            case LIGHT -> world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 8, 0.5, 0.5, 0.5, 0.1);
            case MEDIUM -> world.spawnParticles(ParticleTypes.CRIT, x, y, z, 6, 0.4, 0.4, 0.4, 0.1);
            case HEAVY -> world.spawnParticles(ParticleTypes.SMOKE, x, y, z, 10, 0.4, 0.4, 0.4, 0.05);
            case OVERLOADED -> world.spawnParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 12, 0.5, 0.5, 0.5, 0.02);
            case CRIPPLED -> world.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 8, 0.3, 0.3, 0.3, 0.01);
        }
    }
    
    private static void sendTierChangeMessage(PlayerEntity player, WeightTier oldTier, WeightTier newTier) {
        boolean gettingHeavier = newTier.ordinalValue() > oldTier.ordinalValue();
        
        String message;
        Formatting formatting;
        
        if (gettingHeavier) {
            message = switch (newTier) {
                case MEDIUM -> "Your pack is getting heavy...";
                case HEAVY -> "You're carrying too much!";
                case OVERLOADED -> "You can barely move under this weight!";
                case CRIPPLED -> "You are completely weighed down!";
                default -> "";
            };
            formatting = switch (newTier) {
                case MEDIUM -> Formatting.YELLOW;
                case HEAVY -> Formatting.GOLD;
                case OVERLOADED -> Formatting.RED;
                case CRIPPLED -> Formatting.DARK_RED;
                default -> Formatting.WHITE;
            };
        } else {
            message = switch (newTier) {
                case LIGHT -> "You feel light as a feather!";
                case MEDIUM -> "Your burden has lessened.";
                case HEAVY -> "Still heavy, but manageable.";
                case OVERLOADED -> "Slightly less encumbered.";
                default -> "";
            };
            formatting = switch (newTier) {
                case LIGHT -> Formatting.GREEN;
                case MEDIUM -> Formatting.YELLOW;
                case HEAVY -> Formatting.GOLD;
                case OVERLOADED -> Formatting.RED;
                default -> Formatting.WHITE;
            };
        }
        
        if (!message.isEmpty()) {
            player.sendMessage(Text.literal(message).formatted(formatting), true);
        }
    }
}
