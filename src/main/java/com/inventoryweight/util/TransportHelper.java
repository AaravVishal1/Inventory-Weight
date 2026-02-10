package com.inventoryweight.util;

import com.inventoryweight.config.WeightConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;

public class TransportHelper {
    
    public static boolean isOnTransport(PlayerEntity player) {
        if (!player.hasVehicle()) {
            return false;
        }
        
        Entity vehicle = player.getVehicle();
        if (vehicle == null) {
            return false;
        }
        
        WeightConfig config = WeightConfig.getInstance();
        
        if (config.horseIgnoresWeight && isHorseType(vehicle)) {
            return true;
        }
        
        if (config.minecartIgnoresWeight && vehicle instanceof MinecartEntity) {
            return true;
        }
        
        if (config.boatPartiallyIgnoresWeight && vehicle instanceof BoatEntity) {
            return true;
        }
        
        return false;
    }
    
    public static boolean isOnBoat(PlayerEntity player) {
        if (!player.hasVehicle()) {
            return false;
        }
        
        Entity vehicle = player.getVehicle();
        return vehicle instanceof BoatEntity;
    }
    
    public static boolean isOnMinecart(PlayerEntity player) {
        if (!player.hasVehicle()) {
            return false;
        }
        
        Entity vehicle = player.getVehicle();
        return vehicle instanceof MinecartEntity;
    }
    
    public static boolean isOnHorse(PlayerEntity player) {
        if (!player.hasVehicle()) {
            return false;
        }
        
        Entity vehicle = player.getVehicle();
        return isHorseType(vehicle);
    }
    
    private static boolean isHorseType(Entity entity) {
        return entity instanceof AbstractHorseEntity 
            || entity instanceof AbstractDonkeyEntity 
            || entity instanceof LlamaEntity;
    }
    
    public static float getTransportWeightMultiplier(PlayerEntity player) {
        if (!player.hasVehicle()) {
            return 1.0f;
        }
        
        Entity vehicle = player.getVehicle();
        if (vehicle == null) {
            return 1.0f;
        }
        
        WeightConfig config = WeightConfig.getInstance();
        
        if (config.horseIgnoresWeight && isHorseType(vehicle)) {
            return 0.0f;
        }
        
        if (config.minecartIgnoresWeight && vehicle instanceof MinecartEntity) {
            return 0.0f;
        }
        
        if (config.boatPartiallyIgnoresWeight && vehicle instanceof BoatEntity) {
            return config.boatWeightReduction;
        }
        
        return 1.0f;
    }
}
