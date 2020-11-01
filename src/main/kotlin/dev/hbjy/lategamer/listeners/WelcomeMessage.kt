package dev.hbjy.lategamer.listeners

import com.gitlab.kordlib.core.behavior.channel.createEmbed
import com.gitlab.kordlib.core.behavior.getChannelOf
import com.gitlab.kordlib.core.entity.channel.TextChannel
import com.gitlab.kordlib.core.event.guild.MemberJoinEvent
import com.gitlab.kordlib.kordx.emoji.Emojis
import com.gitlab.kordlib.kordx.emoji.toReaction
import dev.hbjy.lategamer.locale.Messages
import dev.hbjy.lategamer.services.GreetingService
import dev.hbjy.lategamer.services.PersistentData
import kotlinx.coroutines.runBlocking
import me.jakejmattson.discordkt.api.dsl.listeners
import me.jakejmattson.discordkt.api.extensions.toSnowflakeOrNull

fun welcomeMessageListener(persistentData: PersistentData, greetingService: GreetingService) = listeners {
    on<MemberJoinEvent> {
        val (embeds, channel) = persistentData.getGuildProperty(guild.asGuild()) {
            welcomeEmbeds to welcomeChannel
        }
        if (!embeds || channel.isEmpty()) return@on

        @Suppress("BlockingMethodInNonBlockingContext")
        runBlocking {
            val welcomeChannel = channel.toSnowflakeOrNull()?.let { guild.getChannelOf<TextChannel>(it) }
                ?: return@runBlocking

            try {
                val message = welcomeChannel.createEmbed {
                    title = "Welcome"
                    description = Messages.getRandomJoinMessage("${member.mention} (${member.tag})")
                    color = discord.configuration.theme

                    thumbnail {
                        url = member.avatar.url
                    }
                }

                // guildService.addMessageToCache(member.asUser(), message)
                message.addReaction(Emojis.wave.toReaction())
            } catch (ex: Exception) {
                System.err.println(ex.message ?: "Failed to send message.")
            }
        }
    }
}