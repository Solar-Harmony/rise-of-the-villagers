package org.solarharmony.siege

import net.minecraft.server.network.ServerPlayerEntity

/**
 * “Siege” is the name for our custom raid system.
 */
interface SiegeController {
    fun launch(player: ServerPlayerEntity, params: SiegeParameters)
}