package org.solarharmony.entities

import net.minecraft.entity.EntityData
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.ai.goal.ActiveTargetGoal
import net.minecraft.entity.ai.goal.AttackGoal
import net.minecraft.entity.ai.goal.CrossbowAttackGoal
import net.minecraft.entity.ai.goal.MeleeAttackGoal
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.PillagerEntity
import net.minecraft.entity.passive.HorseEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.RangedWeaponItem
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World

/**
 * Custom pillager entity that can be either a footman or a mounted pillager.
 * Footmen use melee attacks, while mounted pillagers use crossbows.
 */
class CustomPillagerEntity(type: EntityType<out PillagerEntity>, world: World) : PillagerEntity(type, world) {
    private var horseMount: HorseEntity? = null

    private val isFootman = this.random.nextBoolean()

    override fun initGoals() {
        super.initGoals()

        // FIXME: footmen can't attack
        if (isFootman) {
            this.goalSelector.clear { it is CrossbowAttackGoal<*> }
            this.goalSelector.add(2, MeleeAttackGoal(this, 1.2, true))
        }

        // turns out spawned pillagers are actually neutral, unless spawned from a raid, outpost or woodland mansion
        this.targetSelector.add(2, ActiveTargetGoal(this, IronGolemEntity::class.java, true))
        this.targetSelector.add(3, ActiveTargetGoal(this, VillagerEntity::class.java, true))
        this.targetSelector.add(4, ActiveTargetGoal(this, ServerPlayerEntity::class.java, true))
    }


    override fun initialize(world: ServerWorldAccess, difficulty: LocalDifficulty, spawnReason: SpawnReason, entityData: EntityData?): EntityData? {
        val result = super.initialize(world, difficulty, spawnReason, entityData)

        if (vehicle == null && isFootman) {
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.IRON_SWORD))
        } else {
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
        horse.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED)?.baseValue = 1.1
        this.startRiding(horse, true)

        this.horseMount = horse
    }
}