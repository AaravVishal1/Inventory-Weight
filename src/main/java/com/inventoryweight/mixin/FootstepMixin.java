package com.inventoryweight.mixin;

import com.inventoryweight.detection.MobDetectionHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Entity.class)
public abstract class FootstepMixin {
    
    @ModifyArg(
            method = "playSound(Lnet/minecraft/sound/SoundEvent;FF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"),
            index = 6
    )
    private float modifyFootstepVolume(float volume) {
        if (!((Object) this instanceof PlayerEntity player)) {
            return volume;
        }
        float multiplier = MobDetectionHandler.getFootstepVolumeMultiplier(player);
        return volume * multiplier;
    }
}
