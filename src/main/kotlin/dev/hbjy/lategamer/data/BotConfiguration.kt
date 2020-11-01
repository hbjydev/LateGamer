package dev.hbjy.lategamer.data

import me.jakejmattson.discordkt.api.dsl.Data

data class BotConfiguration(
    val ownerId: String = "",
    val prefix: String = ""
) : Data("config/bot.json", killIfGenerated = true)