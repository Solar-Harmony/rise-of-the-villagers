package entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin extends IllagerEntity {

    protected PillagerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initEquipment", at = @At("TAIL"))
    private void addArmorEquipment(CallbackInfo ci) {
        // Add iron helmet to pillagers by default for testing
        this.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        System.out.println("DEBUG: PillagerEntity equipped with iron helmet");
    }
}