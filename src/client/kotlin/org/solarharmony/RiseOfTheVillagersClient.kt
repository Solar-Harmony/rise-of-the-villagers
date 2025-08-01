package org.solarharmony

import model.IllagerArmorModel
import model.IllagerBipedModel
import renderer.PillagerBipedRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.entity.mob.IllagerEntity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.solarharmony.entities.registry.EntityRegistry
import org.solarharmony.entities.CustomPillagerEntity
import util.ArmorfulUtil


object RiseOfTheVillagersClient : ClientModInitializer, KoinComponent {

	private val ILLAGER_BIPED: EntityModelLayer =
		ArmorfulUtil.registerEntityModelLayer("illager_biped", IllagerBipedModel<IllagerEntity>::createBodyLayer)

	private val ILLAGER_BIPED_OUTER_ARMOR: EntityModelLayer =
		ArmorfulUtil.registerEntityModelLayer("illager_biped_outer_armor", IllagerArmorModel<IllagerEntity>::createOuterArmorLayer)

	private val ILLAGER_BIPED_INNER_ARMOR: EntityModelLayer =
		ArmorfulUtil.registerEntityModelLayer("illager_biped_inner_armor", IllagerArmorModel<IllagerEntity>::createInnerArmorLayer)

	// ✅ Java-friendly getters
	@JvmStatic fun getIllagerBiped(): EntityModelLayer = ILLAGER_BIPED
	@JvmStatic fun getIllagerBipedOuterArmor(): EntityModelLayer = ILLAGER_BIPED_OUTER_ARMOR
	@JvmStatic fun getIllagerBipedInnerArmor(): EntityModelLayer = ILLAGER_BIPED_INNER_ARMOR

	private val entityRegistry: EntityRegistry by inject()

	override fun onInitializeClient() {
		registerEntityRenderers()
	}

	fun registerEntityRenderers() {
		val entityType = entityRegistry.get(CustomPillagerEntity::class)

		EntityRendererRegistry.register(entityType) { ctx ->
			PillagerBipedRenderer(ctx)
		}
	}
}
