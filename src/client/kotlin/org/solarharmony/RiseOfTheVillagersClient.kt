package org.solarharmony

import NingNongEntityRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.PillagerEntityRenderer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.solarharmony.entities.registry.EntityRegistry
import org.solarharmony.entities.CustomPillagerEntity

object RiseOfTheVillagersClient : ClientModInitializer, KoinComponent {
	private val entityRegistry: EntityRegistry by inject()

	override fun onInitializeClient() {
		registerEntityRenderers()
	}

	/**
	 * Must be done on client once entities are registered.
	 */
	fun registerEntityRenderers() {
		val entityType = entityRegistry.get(CustomPillagerEntity::class)

		EntityRendererRegistry.register(entityType) { ctx ->
			NingNongEntityRenderer(ctx)
		}
	}
}