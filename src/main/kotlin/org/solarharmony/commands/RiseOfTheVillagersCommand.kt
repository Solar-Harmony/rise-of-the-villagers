package org.solarharmony.commands

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.solarharmony.raids.CustomRaidController

object RiseOfTheVillagersCommand : KoinComponent {
    private val raid: CustomRaidController by inject()

    fun register(
        dispatcher: CommandDispatcher<ServerCommandSource>,
        registryAccess: CommandRegistryAccess,
        environment: CommandManager.RegistrationEnvironment
    ) {
        dispatcher.register(
            literal("arisevillagers").requires { it.hasPermissionLevel(2) }.then(
                literal("raid").then(
                    literal("start").executes { ctx ->
                        ctx.source.sendFeedback(
                            { Text.literal("Starting raid...") },
                            false
                        )

                        raid.launch(ctx.source.playerOrThrow)
                        1
                    }
                )
            )
        )
    }
}