package org.solarharmony.raids

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.ai.goal.ActiveTargetGoal
import net.minecraft.entity.ai.goal.CrossbowAttackGoal
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
import net.minecraft.world.World
import org.solarharmony.utils.Globals

class CustomPillagerEntity(type: EntityType<out PillagerEntity>, world: World) : PillagerEntity(type, world) {
    override fun initGoals() {
        super.initGoals()

        this.goalSelector.add(4, MeleeAttackGoal(this, 1.0, true))
        this.targetSelector.add(2, ActiveTargetGoal(this, ServerPlayerEntity::class.java, true))
        this.targetSelector.add(3, ActiveTargetGoal(this, VillagerEntity::class.java, true))
        this.targetSelector.add(4, ActiveTargetGoal(this, IronGolemEntity::class.java, true))
    }

    companion object {
        lateinit var CUSTOM_PILLAGER_KEY: RegistryKey<EntityType<*>>
        lateinit var CUSTOM_PILLAGER: EntityType<CustomPillagerEntity>

        fun register() {
            CUSTOM_PILLAGER_KEY = RegistryKey.of(
                Registries.ENTITY_TYPE.key,
                Identifier.of(Globals.MOD_ID, "custom_pillager")
            )

            CUSTOM_PILLAGER = EntityType.Builder
                .create(::CustomPillagerEntity, SpawnGroup.MONSTER)
                .dimensions(0.6f, 1.95f)
                .maxTrackingRange(32)
                .trackingTickInterval(3)
                .build(CUSTOM_PILLAGER_KEY)

            Registry.register(Registries.ENTITY_TYPE, CUSTOM_PILLAGER_KEY, CUSTOM_PILLAGER)

            createAttributes()
        }

        fun createAttributes() {
            val attr: DefaultAttributeContainer.Builder = MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 24.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.ATTACK_DAMAGE, 5.0)

            FabricDefaultAttributeRegistry.register(
                CUSTOM_PILLAGER,
                attr
            )
        }
    }
}