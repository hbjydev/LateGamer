package dev.hbjy.lategamer.services

import com.gitlab.kordlib.core.entity.Guild
import me.jakejmattson.discordkt.api.annotations.Service

@Service
class GreetingService(private val persistentData: PersistentData) {
    suspend fun isEnabled(guild: Guild) = persistentData.getGuildProperty(guild) {
        welcomeEmbeds
    }

    suspend fun setEnabled(guild: Guild, enabled: Boolean) = persistentData.setGuildProperty(guild) {
        welcomeEmbeds = enabled
    }
}