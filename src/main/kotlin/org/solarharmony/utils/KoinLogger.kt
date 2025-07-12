package org.solarharmony.utils

import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE

// Custom logger for Koin so it prints to Minecraft log
class KoinLogger(
    val logger: org.slf4j.Logger,
    level: Level = Level.INFO
) : Logger(level) {
    override fun display(level: Level, msg: MESSAGE) {
        when (level) {
            Level.DEBUG -> logger.debug(msg)
            Level.ERROR -> logger.error(msg)
            Level.INFO -> logger.info(msg)
            Level.NONE -> {}
            Level.WARNING -> logger.warn(msg)
        }
    }
}