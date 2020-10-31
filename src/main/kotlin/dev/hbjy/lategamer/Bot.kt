package dev.hbjy.lategamer

import com.gitlab.kordlib.gateway.Intent
import com.gitlab.kordlib.kordx.emoji.Emojis
import dev.hbjy.lategamer.services.PersistentData
import dev.hbjy.lategamer.data.BotConfiguration
import me.jakejmattson.discordkt.api.dsl.bot
import me.jakejmattson.discordkt.api.extensions.addField
import java.awt.Color

suspend fun main(args: Array<String>) {
    val token = System.getenv("DISCORD_TOKEN")
    require(token !== null) { "Expected DISCORD_TOKEN to be a valid Discord Token string." }

    bot(token) {
        prefix {
            val persistentData = discord.getInjectionObjects(PersistentData::class)
            guild?.let { persistentData.getGuildProperty(it) { prefix } } ?: "++"
        }

        configure {
            allowMentionPrefix = true
            generateCommandDocs = false
            commandReaction = null
            theme = Color(0x131142)
        }

        mentionEmbed {
            title = "LateGamer"
            color = it.discord.configuration.theme
            description = "A Discord bot written for the Late Gaming Discord server."

            thumbnail {
                url = it.discord.api.getSelf().avatar.url
            }

            footer {
                val versions = it.discord.versions
                text = "DiscordKt ${versions.library} - Kord ${versions.kord} - Kotlin ${versions.kotlin}"
            }

            addField("Prefix", it.prefix())
        }
    }
}