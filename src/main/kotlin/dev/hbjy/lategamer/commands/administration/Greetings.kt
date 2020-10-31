package dev.hbjy.lategamer.commands.administration

import dev.hbjy.lategamer.commands.administration.services.GreetingService
import me.jakejmattson.discordkt.api.arguments.BooleanArg
import me.jakejmattson.discordkt.api.dsl.commands

fun greetingCommands(greetingService: GreetingService) = commands("Greetings") {
    guildCommand("greetings") {
        description = "Toggles greetings on or off for the current guild."

        execute(BooleanArg("enable/disable", "enable", "disable").makeNullableOptional(null)) {
            val (enable) = args

            if (enable !== null) {
                greetingService.setEnabled(guild, enable)
                respond("Welcome embeds are now ${if (enable) "enabled" else "disabled"}.")
            } else {
                val state = greetingService.isEnabled(guild)
                respond("Welcome embeds are currently ${if (state) "enabled" else "disabled"}.")
            }
        }
    }
}