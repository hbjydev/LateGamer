package dev.hbjy.lategamer.data

import me.jakejmattson.discordkt.api.dsl.Data

data class BotConfiguration(val prefix: String = "") : Data("config/config.json", killIfGenerated = true)