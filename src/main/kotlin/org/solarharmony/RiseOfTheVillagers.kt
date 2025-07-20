package org.solarharmony

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.entity.attribute.EntityAttributes
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.slf4j.LoggerFactory
import org.solarharmony.commands.RiseOfTheVillagersCommand
import org.solarharmony.entities.registry.EntityRegisterParams
import org.solarharmony.entities.registry.EntityRegistry
import org.solarharmony.entities.CustomPillagerEntity
import org.solarharmony.utils.KoinLogger

object RiseOfTheVillagers : ModInitializer, KoinComponent {
    private val logger = LoggerFactory.getLogger("rise-of-the-villagers")

	private val entities: EntityRegistry by inject()

	override fun onInitialize() {
		startKoin {
			logger(KoinLogger(logger, Level.INFO))
			modules(appModule)
		}

		val params = EntityRegisterParams(
			attributes = {
				add(EntityAttributes.MAX_HEALTH, 24.0)
				add(EntityAttributes.MOVEMENT_SPEED, 0.3)
				add(EntityAttributes.ATTACK_DAMAGE, 5.0)
			}
		)
		entities.register(CustomPillagerEntity::class, "custom_pillager", params)

		CommandRegistrationCallback.EVENT.register(RiseOfTheVillagersCommand::register);

		logger.info("You are running Rise of the Villagers! Thank you.\n" +
					"Found a bug? Report at https://github.com/Solar-Harmony/rise-of-the-villagers/settings/rules")
	}
}