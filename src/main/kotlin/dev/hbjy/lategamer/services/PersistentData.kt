package dev.hbjy.lategamer.services

import dev.hbjy.lategamer.data.*

import com.gitlab.kordlib.core.entity.Guild
import me.jakejmattson.discordkt.api.annotations.Service

@Service
class PersistentData(private val configuration: Configuration,
                     private val botConfiguration: BotConfiguration) {
    fun getGuilds() = configuration.guildConfigurations

    fun <R> setGlobalProperty(fn: Configuration.() -> R) =
        fn(configuration).also { configuration.save() }

    fun <R> getGlobalProperty(fn: Configuration.() -> R) =
        configuration.let(fn)

    suspend fun <R> setGuildProperty(guild: Guild, fn: suspend GuildConfiguration.() -> R): R {
        val config = getGuildConfig(guild)
        return fn(config).also { configuration.save() }
    }

    fun <R> setGuildProperty(guild: String, fn: GuildConfiguration.() -> R): R {
        val config = getGuildConfig(guild)
        return fn(config).also { configuration.save() }
    }

    suspend fun <R> getGuildProperty(guild: Guild, fn: suspend GuildConfiguration.() -> R) =
        fn(getGuildConfig(guild))

    private fun getGuildConfig(guildId: String): GuildConfiguration {
        val guild = configuration.guildConfigurations.find { it.guildId == guildId }

        if (guild != null) {
            return guild
        }

        configuration.guildConfigurations.add(GuildConfiguration(guildId, botConfiguration.prefix))
        configuration.save()

        return configuration.guildConfigurations.first { it.guildId == guildId }
    }

    private fun getGuildConfig(guild: Guild) = getGuildConfig(guild.id.value)
}