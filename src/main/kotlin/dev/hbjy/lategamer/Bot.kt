package dev.hbjy.lategamer

import com.gitlab.kordlib.gateway.Intent
import com.gitlab.kordlib.gateway.PrivilegedIntent
import dev.hbjy.lategamer.services.PermissionService
import dev.hbjy.lategamer.services.PersistentData
import me.jakejmattson.discordkt.api.dsl.bot
import me.jakejmattson.discordkt.api.extensions.addField

@OptIn(PrivilegedIntent::class)
suspend fun main() {
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
            // theme = Color(0x131142)
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

        permissions {
            val permissionService = discord.getInjectionObjects(PermissionService::class)

            if (guild != null) {
                permissionService.isCommandVisible(guild!!, user, command)
            } else {
                false
            }
        }

        intents {
            +Intent.GuildMessages
            +Intent.GuildMembers
        }
    }
}