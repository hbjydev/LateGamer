package dev.hbjy.lategamer.data

import me.jakejmattson.discordkt.api.dsl.Data

data class GuildConfiguration(val guildId: String = "",
                              var prefix: String,
                              var cooldown: Int = 5,
                              var welcomeEmbeds: Boolean = false,
                              var welcomeChannel: String = "",
                              val macros: MutableMap<String, TextMacro> = mutableMapOf())

data class Configuration(
    val guildConfigurations: MutableList<GuildConfiguration> = mutableListOf()
) : Data("config/guilds.json", killIfGenerated = false)

data class TextMacro(val name: String = "", var contents: String = "", var category: String = "")