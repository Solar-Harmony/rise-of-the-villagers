package mixin;

import com.google.common.collect.Maps;
import loot.ArmorfulLootTables;
import util.ArmorfulUtil;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PillagerEntity.class)
public abstract class PillagerArmorMixin extends PatrolEntity {
    @Unique
    private static final Map<EquipmentSlot, Identifier> EQUIPMENT_SLOT_ITEMS = Util.make(Maps.newHashMap(), (slotItems) -> {
        slotItems.put(EquipmentSlot.HEAD, ArmorfulLootTables.ILLAGER_HELMET);
        slotItems.put(EquipmentSlot.CHEST, ArmorfulLootTables.ILLAGER_CHEST);
        slotItems.put(EquipmentSlot.LEGS, ArmorfulLootTables.ILLAGER_LEGGINGS);
        slotItems.put(EquipmentSlot.FEET, ArmorfulLootTables.ILLAGER_FEET);
    });

    protected PillagerArmorMixin(EntityType<? extends PillagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At("TAIL"))
    public void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        if (world instanceof ServerWorld) {
            ArmorfulUtil.giveArmorNaturally(this.random, this, difficulty);
        }
    }

    @Unique
    public List<ItemStack> getItemsFromLootTable(EquipmentSlot slot) {
        if (EQUIPMENT_SLOT_ITEMS.containsKey(slot)) {
            LootTable loot = this.getWorld().getServer().getReloadableRegistries().getLootTable(
                    RegistryKey.of(RegistryKeys.LOOT_TABLE, EQUIPMENT_SLOT_ITEMS.get(slot))
            );
            LootWorldContext.Builder builder = new LootWorldContext.Builder((ServerWorld) this.getWorld())
                    .add(LootContextParameters.THIS_ENTITY, this);
            return loot.generateLoot(builder.build(ArmorfulLootTables.SLOT));
        } else {
            return List.of(); // return empty list instead of null
        }
    }
}
