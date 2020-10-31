package dev.hbjy.lategamer

import com.gitlab.kordlib.gateway.Intent
import com.gitlab.kordlib.kordx.emoji.Emojis
import me.jakejmattson.discordkt.api.dsl.bot
import me.jakejmattson.discordkt.api.extensions.addField
import me.jakejmattson.discordkt.api.extensions.profileLink
import java.awt.Color
import kotlin.system.exitProcess

suspend fun main(args: Array<String>) {
    val token = System.getenv("DISCORD_TOKEN")
    if (token == null) {
        System.err.println("Please include a Discord token in the DISCORD_TOKEN environment variable.")
        exitProcess(1)
    }

    bot(token) {
        prefix { "lg!" }

        configure {
            allowMentionPrefix = LateGamer.Misc.allowMentionPrefix

            generateCommandDocs = false
            showStartupLog = true
            recommendCommands = true
            commandReaction = Emojis.videoGame

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

        intents {
            +Intent.GuildMessages
        }
    }
}