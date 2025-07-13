package org.solarharmony

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.slf4j.LoggerFactory
import org.solarharmony.commands.RiseOfTheVillagersCommand
import org.solarharmony.utils.KoinLogger

object RiseOfTheVillagers : ModInitializer {
    private val logger = LoggerFactory.getLogger("rise-of-the-villagers")

	override fun onInitialize() {
		startKoin {
			logger(KoinLogger(logger, Level.INFO))
			modules(appModule)
		}

		CommandRegistrationCallback.EVENT.register(RiseOfTheVillagersCommand::register);

		logger.info("You are running Rise of the Villagers! Thank you.\n" +
					"Found a bug? Report at https://github.com/Solar-Harmony/rise-of-the-villagers/settings/rules")
	}
}