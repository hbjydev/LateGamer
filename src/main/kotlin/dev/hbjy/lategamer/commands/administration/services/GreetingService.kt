package dev.hbjy.lategamer.commands.administration.services

import com.gitlab.kordlib.core.behavior.getChannelOfOrNull
import com.gitlab.kordlib.core.entity.*
import com.gitlab.kordlib.core.entity.channel.TextChannel
import dev.hbjy.lategamer.services.PersistentData
import dev.hbjy.lategamer.utils.EvictingQueue
import me.jakejmattson.discordkt.api.annotations.Service
import me.jakejmattson.discordkt.api.extensions.toSnowflakeOrNull

@Service
class GreetingService(private val persistentData: PersistentData) {
    companion object {
        private val welcomeMessages: MutableMap<Long, EvictingQueue<Pair<Long, Long>>> = mutableMapOf()
    }

    suspend fun addMessageToCache(user: User, msg: Message) {
        if (welcomeMessages.containsKey(msg.getGuild().id.longValue)) {
            welcomeMessages[msg.getGuild().id.longValue]!!.add(user.id.longValue to msg.id.longValue)
        } else {
            welcomeMessages[msg.getGuild().id.longValue] = EvictingQueue.create(200)
            welcomeMessages[msg.getGuild().id.longValue]!!.add(user.id.longValue to msg.id.longValue)
        }
    }

    fun getCachedMessage(guild: Guild, user: User) =
        welcomeMessages[guild.id.longValue]?.find { it.first == user.id.longValue }?.second

    fun removeMessagesFromCache(guild: Guild, user: User) =
        welcomeMessages[guild.id.longValue]?.removeIf { it.first == user.id.longValue }

    suspend fun setEnabled(guild: Guild, state: Boolean) = persistentData.setGuildProperty(guild) {
        welcomeEmbeds = state
    }

    suspend fun isEnabled(guild: Guild) = persistentData.getGuildProperty(guild) {
        welcomeEmbeds
    }

    suspend fun setChannel(guild: Guild, textChannel: TextChannel) = persistentData.setGuildProperty(guild) {
        welcomeChannel = textChannel.id.value
    }

    suspend fun getChannel(guild: Guild) = persistentData.getGuildProperty(guild) { welcomeChannel }.let {
        it.ifBlank { null }.let { id ->
            id?.toSnowflakeOrNull()?.let { guild.getChannelOfOrNull<TextChannel>(it) }
        }
    }
}