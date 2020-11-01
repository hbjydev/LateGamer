package dev.hbjy.lategamer.data

import dev.hbjy.lategamer.services.PermissionLevel
import me.jakejmattson.discordkt.api.dsl.Data

data class GuildConfiguration(val guildId: String = "",
                              var prefix: String = "++",
                              var cooldown: Int = 5,
                              var welcomeEmbeds: Boolean = false,
                              var welcomeChannel: String = "",
                              var botChannel: String = "",
                              var loggingChannel: String = "",
                              var muteRole: String = "",
                              var softMuteRole: String = "",
                              var totalCommandsExecuted: Int = 0,
                              val grantableRoles: MutableMap<String, MutableList<String>> = mutableMapOf(),
                              val rolePermissions: MutableMap<String, PermissionLevel> = mutableMapOf(),
                              val commandPermission: MutableMap<String, PermissionLevel> = mutableMapOf(),
                              val assignedColorRoles: MutableMap<String, MutableList<String>> = mutableMapOf(),
                              val availableMacros: MutableMap<String, TextMacro> = mutableMapOf())

data class Configuration(
    val guildConfigurations: MutableList<GuildConfiguration> = mutableListOf()
) : Data("config/guilds.json", killIfGenerated = false)

data class TextMacro(val name: String = "", var contents: String = "", var category: String = "")