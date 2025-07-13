package org.solarharmony.raids

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.ai.goal.ActiveTargetGoal
import net.minecraft.entity.ai.goal.MeleeAttackGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.PillagerEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec2f
import net.minecraft.world.World
import org.solarharmony.utils.Globals
import kotlin.reflect.KClass

interface EntityRegistry {
    fun <T : LivingEntity> get(type: KClass<T>): EntityType<T>
    fun register(type: KClass<out Entity>, name: String, params: EntityRegisterParams = EntityRegisterParams())
}

data class EntityRegisterParams(
    val spawnGroup: SpawnGroup = SpawnGroup.MONSTER,
    val size: Vec2f = Vec2f(0.6f, 1.95f),
    val maxTrackingRange: Int = 32,
    val trackingTickInterval: Int = 3,
    val attributes: DefaultAttributeContainer.Builder.() -> Unit = {}
)

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

        val registryType: EntityType<CustomPillagerEntity?>? = EntityType.Builder
            .create(::CustomPillagerEntity, params.spawnGroup)
            .dimensions(params.size.x, params.size.y)
            .maxTrackingRange(params.maxTrackingRange)
            .trackingTickInterval(params.trackingTickInterval)
            .build(registryKey)
            ?: throw IllegalArgumentException("Entity type for ${type.simpleName} could not be created")

        Registry.register(Registries.ENTITY_TYPE, registryKey, registryType)

        FabricDefaultAttributeRegistry.register(
            registryType,
            MobEntity.createMobAttributes().apply {
                params.attributes(this)
            }
        )

        entityKeys[type] = registryKey
    }

    private val entityKeys: MutableMap<KClass<out Entity>, RegistryKey<EntityType<*>>> = mutableMapOf()
}

class CustomPillagerEntity(type: EntityType<out PillagerEntity>, world: World) : PillagerEntity(type, world) {
    override fun initGoals() {
        super.initGoals()

        this.goalSelector.add(4, MeleeAttackGoal(this, 1.0, true))
        this.targetSelector.add(2, ActiveTargetGoal(this, ServerPlayerEntity::class.java, true))
        this.targetSelector.add(3, ActiveTargetGoal(this, VillagerEntity::class.java, true))
        this.targetSelector.add(4, ActiveTargetGoal(this, IronGolemEntity::class.java, true))
    }
}