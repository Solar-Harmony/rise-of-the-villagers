package org.solarharmony.raids

import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.ai.goal.ActiveTargetGoal
import net.minecraft.entity.ai.goal.CrossbowAttackGoal
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Direction
import net.minecraft.world.Heightmap
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.solarharmony.utils.plus
import org.solarharmony.utils.times
import org.solarharmony.utils.unaryMinus
import org.solarharmony.utils.with

class CustomRaidControllerTest : CustomRaidController, KoinComponent {
    private val entityRegistry: EntityRegistry by inject()

    override fun launch(player: ServerPlayerEntity) {
        val world = player.world

        val spawnDistance = 10.0;
        val baseSpawnPos = player.pos + -player.rotationVector * spawnDistance
        val adjustedY = world.getTopY(Heightmap.Type.WORLD_SURFACE, baseSpawnPos.x.toInt(), baseSpawnPos.z.toInt()).toDouble()
        val spawnPos = baseSpawnPos.with(y = adjustedY)

        val entityType = entityRegistry.get(CustomPillagerEntity::class)

        for (i in 0 until 10) {
            val pillager = entityType.create(world, SpawnReason.EVENT)
                ?: return

            pillager.updatePosition(spawnPos.x, spawnPos.y, spawnPos.z)
            pillager.equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.IRON_SWORD))
            pillager.initialize(world, world.getLocalDifficulty(pillager.blockPos), SpawnReason.EVENT, null)
            world.spawnEntity(pillager)
        }
    }
}