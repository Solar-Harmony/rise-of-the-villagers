package org.solarharmony.entities.registry

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.solarharmony.entities.CustomPillagerEntity
import org.solarharmony.utils.Globals
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class EntityRegistryImpl : EntityRegistry {
    @Suppress("UNCHECKED_CAST")
    override fun <T : LivingEntity> get(type: KClass<T>): EntityType<T> {
        val key = entityKeys[type]
            ?: throw IllegalArgumentException("Custom entity type for ${type.simpleName} not registered")

        val entityType = Registries.ENTITY_TYPE.get(key)
            ?: throw IllegalStateException("Entity type ${type.simpleName} not found in registry")

        return entityType as EntityType<T>
    }

    override fun register(type: KClass<out Entity>, name: String, params: EntityRegisterParams) {
        val registryKey = RegistryKey.of(
            Registries.ENTITY_TYPE.key,
            Identifier.of(Globals.MOD_ID, name)
        )

        val constructor = type.primaryConstructor
            ?: throw IllegalArgumentException("Entity type ${type.simpleName} must have a primary constructor")

        val factory = { entityType: EntityType<out Entity>, world: World ->
            constructor.call(entityType, world)
        }

        val registryType = EntityType.Builder
            .create(factory, params.spawnGroup)
            .dimensions(params.size.x, params.size.y)
            .maxTrackingRange(params.maxTrackingRange)
            .trackingTickInterval(params.trackingTickInterval)
            .build(registryKey)
            ?: throw IllegalArgumentException("Entity type for ${type.simpleName} could not be created")

        Registry.register(Registries.ENTITY_TYPE, registryKey, registryType)

        @Suppress("UNCHECKED_CAST")
        FabricDefaultAttributeRegistry.register(
            registryType as EntityType<out LivingEntity>,
            MobEntity.createMobAttributes().apply {
                params.attributes(this)
            }
        )

        entityKeys[type] = registryKey
    }

    private val entityKeys: MutableMap<KClass<out Entity>, RegistryKey<EntityType<*>>> = mutableMapOf()
}