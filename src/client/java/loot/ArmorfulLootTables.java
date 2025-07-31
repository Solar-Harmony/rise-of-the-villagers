//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package loot;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import util.ArmorfulUtil;
import java.util.function.Consumer;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextType;

public class ArmorfulLootTables {
    public static final Identifier ILLAGER_HELMET = ArmorfulUtil.id("entities/illager_helmet");
    public static final Identifier ILLAGER_CHEST = ArmorfulUtil.id("entities/illager_chestplate");
    public static final Identifier ILLAGER_LEGGINGS = ArmorfulUtil.id("entities/illager_legs");
    public static final Identifier ILLAGER_FEET = ArmorfulUtil.id("entities/illager_feet");
    public static final Identifier NATURAL_SPAWN_HELMET = ArmorfulUtil.id("entities/natural_spawn/helmet");
    public static final Identifier NATURAL_SPAWN_CHEST = ArmorfulUtil.id("entities/natural_spawn/chestplate");
    public static final Identifier NATURAL_SPAWN_LEGGINGS = ArmorfulUtil.id("entities/natural_spawn/legs");
    public static final Identifier NATURAL_SPAWN_FEET = ArmorfulUtil.id("entities/natural_spawn/feet");
    public static final BiMap<Identifier, ContextType> REGISTRY = HashBiMap.create();
    public static final ContextType SLOT = register("slot", (table) -> table.require(LootContextParameters.THIS_ENTITY));

    public static ContextType register(String id, Consumer<ContextType.Builder> lootContextBuilder) {
        ContextType.Builder LootContextType$builder = new ContextType.Builder();
        lootContextBuilder.accept(LootContextType$builder);
        ContextType LootContextType = LootContextType$builder.build();
        Identifier Identifier = ArmorfulUtil.id(id);
        ContextType LootContextType1 = (ContextType)REGISTRY.put(Identifier, LootContextType);
        if (LootContextType1 != null) {
            throw new IllegalStateException("Loot table parameter set " + String.valueOf(Identifier) + " is already registered");
        } else {
            return LootContextType;
        }
    }
}
