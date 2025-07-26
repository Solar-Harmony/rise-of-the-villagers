package org.solarharmony.mixin;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.jetbrains.annotations.Nullable;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin {
    @Inject(method = "initialize", at = @At("RETURN"))
    private void onInitialize(
            ServerWorldAccess world,
            LocalDifficulty difficulty,
            SpawnReason spawnReason,
            @Nullable EntityData entityData,
            CallbackInfoReturnable<EntityData> cir
    ) {
        PillagerEntity pillager = (PillagerEntity)(Object)this;
        ItemStack helmet = new ItemStack(Items.IRON_HELMET);
        pillager.equipStack(EquipmentSlot.HEAD, helmet);
        pillager.setEquipmentDropChance(EquipmentSlot.HEAD, 0.0f);

        System.out.println("Helmet applied and locked to Pillager: " + pillager.getEquippedStack(EquipmentSlot.HEAD));
    }

    @Inject(method = "initialize", at = @At("RETURN"))
    private void debugHelmetAfterInit(
            ServerWorldAccess world,
            LocalDifficulty difficulty,
            SpawnReason spawnReason,
            @Nullable EntityData entityData,
            CallbackInfoReturnable<EntityData> cir
    ) {
        PillagerEntity pillager = (PillagerEntity)(Object)this;

        // Schedule a check for next tick to ensure equipment is fully applied
        world.getServer().execute(() -> {
            ItemStack headItem = pillager.getEquippedStack(EquipmentSlot.HEAD);
            if (!headItem.isEmpty()) {
                System.out.println("DEBUG: PILLAGER IS WEARING HELMET: " + headItem);
            } else {
                System.out.println("DEBUG: PILLAGER HAS NO HELMET!");
            }
        });
    } //helmet 100% works, but not rendered in the game so fuckmy life
}