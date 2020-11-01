package dev.hbjy.lategamer.commands.administration

import com.gitlab.kordlib.core.entity.channel.TextChannel
import dev.hbjy.lategamer.services.GreetingService
import dev.hbjy.lategamer.services.PermissionLevel
import dev.hbjy.lategamer.services.requiredPermissionLevel
import me.jakejmattson.discordkt.api.arguments.BooleanArg
import me.jakejmattson.discordkt.api.arguments.ChannelArg
import me.jakejmattson.discordkt.api.dsl.commands
import me.jakejmattson.discordkt.api.dsl.precondition

fun greetingCommands(greetingService: GreetingService) = commands("Greetings") {
    guildCommand("greetings") {
        description = "Toggles greetings on or off for the current guild."
        requiredPermissionLevel = PermissionLevel.Administrator

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

    guildCommand("greetingchannel") {
        description = "Sets the channel for greetings."
        requiredPermissionLevel = PermissionLevel.Administrator

        execute(ChannelArg("channel", false)) {
            val (channel) = args

            greetingService.setChannel(guild, channel as TextChannel)
            respond("Welcome embeds will now be shown in the channel ${channel.mention}.")
        }
    }
}