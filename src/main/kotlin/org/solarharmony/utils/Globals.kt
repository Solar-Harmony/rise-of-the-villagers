package org.solarharmony.utils

import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader

object Globals {
    const val MOD_ID = "solarharmony_rotv"

    val isClient: Boolean = FabricLoader.getInstance().environmentType == EnvType.CLIENT
}