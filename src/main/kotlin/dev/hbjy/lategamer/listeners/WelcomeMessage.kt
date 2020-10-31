package dev.hbjy.lategamer.listeners

import com.gitlab.kordlib.core.behavior.channel.createEmbed
import com.gitlab.kordlib.core.behavior.channel.createMessage
import com.gitlab.kordlib.core.behavior.getChannelOf
import com.gitlab.kordlib.core.entity.channel.MessageChannel
import com.gitlab.kordlib.core.entity.channel.TextChannel
import com.gitlab.kordlib.core.event.guild.MemberJoinEvent
import dev.hbjy.lategamer.services.GreetingService
import dev.hbjy.lategamer.services.PersistentData
import me.jakejmattson.discordkt.api.dsl.listeners
import me.jakejmattson.discordkt.api.extensions.toSnowflakeOrNull

fun welcomeMessageListener(persistentData: PersistentData, greetingService: GreetingService) = listeners {
    on<MemberJoinEvent> {
        val (embeds, channel) = persistentData.getGuildProperty(this.getGuild()) {
            welcomeEmbeds to welcomeChannel
        }
        if (!embeds || channel.isEmpty()) return@on

        val welcomeChannel = channel.toSnowflakeOrNull()?.let { guild.getChannelOf<TextChannel>(it) } ?: return@on

        val message = "Well, well, well, if it isn't {user}!".replace(
            "{user}",
            "${member.mention} (${member.tag})"
        )

        try {
            welcomeChannel.createEmbed {
                title = "New member!"
                description = message
                thumbnail {
                    url = member.avatar.url
                }
            }
        } catch (ex: Exception) {
            System.err.println(ex.message ?: "Failed to send welcome message.")
        }
    }
}