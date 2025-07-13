package org.solarharmony

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.PillagerEntityRenderer
import org.solarharmony.raids.CustomPillagerEntity.Companion.CUSTOM_PILLAGER

object RiseOfTheVillagersClient : ClientModInitializer {
	override fun onInitializeClient() {
		EntityRendererRegistry.register(CUSTOM_PILLAGER) { ctx ->
			PillagerEntityRenderer(ctx)
		}
	}
}