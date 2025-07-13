package org.solarharmony.raids

import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Direction
import net.minecraft.world.Heightmap
import org.solarharmony.utils.plus
import org.solarharmony.utils.times
import org.solarharmony.utils.unaryMinus
import org.solarharmony.utils.with

class CustomRaidControllerTest : CustomRaidController {
    override fun launch(player: ServerPlayerEntity) {
        val world = player.world

        val spawnDistance = 10.0;
        val baseSpawnPos = player.pos + -player.rotationVector * spawnDistance
        val adjustedY = world.getTopY(Heightmap.Type.WORLD_SURFACE, baseSpawnPos.x.toInt(), baseSpawnPos.z.toInt()).toDouble()
        val spawnPos = baseSpawnPos.with(y = adjustedY)

        for (i in 0 until 10) {
            val pillager = EntityType.PILLAGER.create(world, SpawnReason.EVENT)
                ?: return

            pillager.updatePosition(spawnPos.x, spawnPos.y, spawnPos.z)
            world.spawnEntity(pillager)
        }
    }
}