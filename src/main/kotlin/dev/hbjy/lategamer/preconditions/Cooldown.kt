package dev.hbjy.lategamer.preconditions

import dev.hbjy.lategamer.services.PermissionLevel
import dev.hbjy.lategamer.services.PermissionService
import dev.hbjy.lategamer.services.PersistentData
import me.jakejmattson.discordkt.api.dsl.precondition
import org.joda.time.DateTime

val cooldownMap = mutableMapOf<Long, Long>()

fun cooldownPrecondition(persistentData: PersistentData,
             permissionService: PermissionService) = precondition {
    command ?: return@precondition

    val member = guild?.getMember(author.id)

    if (member != null && permissionService.getPermissionLevel(member) >= PermissionLevel.Staff)
        return@precondition

    val cd = guild?.let {
        persistentData.getGuildProperty(it) { cooldown }
    } ?: 5

    if (cooldownMap[author.id.longValue] != null) {
        val diff = DateTime.now().millis - cooldownMap[author.id.longValue]!!

        if (diff < cd * 1000) {
            return@precondition fail("You're doing that too quickly. Slow down! (${String.format("%.2f", (cd * 1000 - diff) / 1000.0f)}s)")
        }
    }

    cooldownMap[author.id.longValue] = DateTime.now().millis
}