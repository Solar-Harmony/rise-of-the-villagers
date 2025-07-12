package org.solarharmony

import net.fabricmc.api.ModInitializer
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger
import org.slf4j.LoggerFactory
import org.solarharmony.utils.KoinLogger

object RiseOfTheVillagers : ModInitializer {
    private val logger = LoggerFactory.getLogger("rise-of-the-villagers")

	override fun onInitialize() {
		startKoin {
			logger(KoinLogger(logger, Level.INFO))
			modules(appModule)
		}

		logger.info("You are running Rise of the Villagers! Thank you.\n" +
					"Found a bug? Report at https://github.com/Solar-Harmony/rise-of-the-villagers/settings/rules")
	}
}