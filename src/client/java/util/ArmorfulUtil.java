package util;

import com.google.common.collect.Maps;
import loot.ArmorfulLootTables;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EquipmentSlot.Type;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;

public class ArmorfulUtil {
    public static final Map<EquipmentSlot, Identifier> NATURAL_SPAWN_EQUIPMENT_SLOT_ITEMS = (Map)Util.make(Maps.newHashMap(), (slotItems) -> {
        slotItems.put(EquipmentSlot.HEAD, ArmorfulLootTables.NATURAL_SPAWN_HELMET);
        slotItems.put(EquipmentSlot.CHEST, ArmorfulLootTables.NATURAL_SPAWN_CHEST);
        slotItems.put(EquipmentSlot.LEGS, ArmorfulLootTables.NATURAL_SPAWN_LEGGINGS);
        slotItems.put(EquipmentSlot.FEET, ArmorfulLootTables.NATURAL_SPAWN_FEET);
    });

    public static Identifier id(String id) {
        return Identifier.tryParse("armorful", id);
    }

    public static Identifier defaultID(String id) {
        return Identifier.tryParse("minecraft", id);
    }

    public static EntityModelLayer registerEntityModelLayer(String id, EntityModelLayerRegistry.TexturedModelDataProvider texturedModelDataProvider) {
        EntityModelLayer modelLayer = new EntityModelLayer(id(id), id);
        EntityModelLayerRegistry.registerModelLayer(modelLayer, texturedModelDataProvider);
        return modelLayer;
    }

    public static List<ItemStack> getNaturalSpawnItemsFromLootTable(LivingEntity entity, EquipmentSlot slot) {
        if (NATURAL_SPAWN_EQUIPMENT_SLOT_ITEMS.containsKey(slot)) {
            LootTable loot = entity.getWorld().getServer().getReloadableRegistries().getLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, (Identifier)NATURAL_SPAWN_EQUIPMENT_SLOT_ITEMS.get(slot)));
            LootWorldContext.Builder lootcontext$builder = (new LootWorldContext.Builder((ServerWorld)entity.getWorld())).add(LootContextParameters.THIS_ENTITY, entity);
            return loot.generateLoot(lootcontext$builder.build(ArmorfulLootTables.SLOT));
        } else {
            return null;
        }
    }

    public static void giveArmorNaturally(Random random, LivingEntity entity, LocalDifficulty difficulty) {
        if (random.nextFloat() < 0.24F * difficulty.getClampedLocalDifficulty()) {
            float difficultyChance = entity.getWorld().getDifficulty() == Difficulty.HARD ? 0.1F : 0.25F;
            boolean flag = true;

            for(EquipmentSlot slotType : EquipmentSlot.values()) {
                if (slotType.getType() == Type.HUMANOID_ARMOR) {
                    if (!flag && random.nextFloat() < difficultyChance) {
                        break;
                    }

                    flag = false;

                    for(ItemStack stack : getNaturalSpawnItemsFromLootTable(entity, slotType)) {
                        entity.equipStack(slotType, stack);
                    }
                }
            }
        }

    }
}