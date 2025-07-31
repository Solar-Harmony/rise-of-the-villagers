package org.solarharmony

import model.IllagerArmorModel
import model.IllagerBipedModel
import renderer.PillagerBipedRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.model.EntityModelLayer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.solarharmony.entities.registry.EntityRegistry
import org.solarharmony.entities.CustomPillagerEntity
import util.ArmorfulUtil


object RiseOfTheVillagersClient : ClientModInitializer, KoinComponent {

	val ILLAGER_BIPED: EntityModelLayer = ArmorfulUtil.registerEntityModelLayer("illager_biped", IllagerBipedModel::createBodyLayer)
	val ILLAGER_BIPED_OUTER_ARMOR: EntityModelLayer = ArmorfulUtil.registerEntityModelLayer("illager_biped_outer_armor", IllagerArmorModel::createOuterArmorLayer)
	val ILLAGER_BIPED_INNER_ARMOR: EntityModelLayer = ArmorfulUtil.registerEntityModelLayer("illager_biped_inner_armor", IllagerArmorModel::createInnerArmorLayer)



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
			PillagerBipedRenderer(ctx)
		}
	}
}