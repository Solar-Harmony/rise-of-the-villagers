package org.solarharmony.siege

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.SpawnReason
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d
import net.minecraft.world.Heightmap
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.solarharmony.entities.registry.EntityRegistry
import org.solarharmony.entities.CustomPillagerEntity
import org.solarharmony.utils.plus
import org.solarharmony.utils.times
import org.solarharmony.utils.unaryMinus
import org.solarharmony.utils.with

class SiegeControllerTest : SiegeController, KoinComponent {
    private val entityRegistry: EntityRegistry by inject()

    /**
     * This is a test implementation of the SiegeController.
     * It spawns 10 pillagers around the player when launched.
     */
    override fun launch(player: ServerPlayerEntity, params: SiegeParameters) {
        val world = player.world

        val baseSpawnPos = player.pos + -player.rotationVector * params.spawnDistance
        val adjustedY = world.getTopY(Heightmap.Type.WORLD_SURFACE, baseSpawnPos.x.toInt(), baseSpawnPos.z.toInt()).toDouble()
        val spawnPos = baseSpawnPos.with(y = adjustedY)

        val entityType = entityRegistry.get(CustomPillagerEntity::class)

        for (i in 0 until params.pillagerCount) {
            val pillager = entityType.create(world, SpawnReason.EVENT)
                ?: return

            val offset = i.toDouble() // to spawn them in a straight line

            pillager.updatePosition(spawnPos.x + offset, spawnPos.y + offset, spawnPos.z)
            pillager.equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.IRON_SWORD))
            pillager.initialize(world, world.getLocalDifficulty(pillager.blockPos), SpawnReason.EVENT, null)
            world.spawnEntity(pillager)
        }
    }
}