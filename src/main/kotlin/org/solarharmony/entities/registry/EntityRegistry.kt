package org.solarharmony.entities.registry

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import kotlin.reflect.KClass

/**
 * Abstracts registration of custom entities
 * Provides type-safe non-static injected access to registered entity types
 */
interface EntityRegistry {
    fun <T : LivingEntity> get(type: KClass<T>): EntityType<T>
    fun register(type: KClass<out Entity>, name: String, params: EntityRegisterParams = EntityRegisterParams())
}