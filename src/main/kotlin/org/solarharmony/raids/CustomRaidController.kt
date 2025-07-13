package org.solarharmony.raids

import net.minecraft.server.network.ServerPlayerEntity

interface CustomRaidController {
    fun launch(player: ServerPlayerEntity)
}