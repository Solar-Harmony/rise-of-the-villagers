package org.solarharmony.entities.registry

import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.util.math.Vec2f

data class EntityRegisterParams(
    val spawnGroup: SpawnGroup = SpawnGroup.MONSTER,
    val size: Vec2f = Vec2f(0.6f, 1.95f),
    val maxTrackingRange: Int = 32,
    val trackingTickInterval: Int = 3,
    val attributes: DefaultAttributeContainer.Builder.() -> Unit = {}
)