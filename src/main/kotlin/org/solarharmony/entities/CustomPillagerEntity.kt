package org.solarharmony.entities

import net.minecraft.entity.EntityData
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.ai.goal.ActiveTargetGoal
import net.minecraft.entity.ai.goal.CrossbowAttackGoal
import net.minecraft.entity.ai.goal.MeleeAttackGoal
import net.minecraft.entity.mob.PillagerEntity
import net.minecraft.entity.passive.HorseEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World

class CustomPillagerEntity(type: EntityType<out PillagerEntity>, world: World) : PillagerEntity(type, world) {
    private var horseMount: HorseEntity? = null

    override fun initGoals() {
        super.initGoals()

        // clear default goals
        this.goalSelector.clear { it is CrossbowAttackGoal<*> }

        // add our own goals
        this.goalSelector.add(4, MeleeAttackGoal(this, 1.0, true))
        this.targetSelector.add(2, ActiveTargetGoal(this, ServerPlayerEntity::class.java, true))
        this.targetSelector.add(3, ActiveTargetGoal(this, VillagerEntity::class.java, true))
        this.targetSelector.add(4, ActiveTargetGoal(this, IronGolemEntity::class.java, true))
    }

    override fun initialize(world: ServerWorldAccess, difficulty: LocalDifficulty, spawnReason: SpawnReason, entityData: EntityData?): EntityData? {
        val result = super.initialize(world, difficulty, spawnReason, entityData)

        if (this.vehicle == null) {
            spawnHorseMount()
        }

        return result
    }

    /**
     * Experimental: gives pillagers a horse mount.
     * FIXME: It works, but the rider floats above the saddle and I couldn't find why.
     */
    private fun spawnHorseMount() {
        if (this.world.isClient) return

        val horse = HorseEntity(EntityType.HORSE, this.world)
        horse.refreshPositionAndAngles(this.x, this.y, this.z, this.yaw, 0.0f)
        horse.initialize(
            this.world as ServerWorldAccess,
            this.world.getLocalDifficulty(this.blockPos),
            SpawnReason.JOCKEY,
            null
        )

        horse.owner = this
        horse.isTame = true
        horse.isBred = true

        this.world.spawnEntity(horse)
        this.startRiding(horse, true)

        this.horseMount = horse
    }
}