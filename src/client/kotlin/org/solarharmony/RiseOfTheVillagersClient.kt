package org.solarharmony

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.PillagerEntityRenderer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.solarharmony.raids.CustomPillagerEntity
import org.solarharmony.raids.EntityRegistry

object RiseOfTheVillagersClient : ClientModInitializer, KoinComponent {
	private val entityRegistry: EntityRegistry by inject()

	override fun onInitializeClient() {
		val entityType = entityRegistry.get(CustomPillagerEntity::class)

		EntityRendererRegistry.register(entityType) { ctx ->
			PillagerEntityRenderer(ctx)
		}
	}
}