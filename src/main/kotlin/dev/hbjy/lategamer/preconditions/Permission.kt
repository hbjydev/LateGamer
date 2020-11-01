package dev.hbjy.lategamer.preconditions

import dev.hbjy.lategamer.locale.Messages
import dev.hbjy.lategamer.services.PermissionLevel
import dev.hbjy.lategamer.services.PermissionService
import dev.hbjy.lategamer.services.PersistentData
import dev.hbjy.lategamer.services.requiredPermissionLevel
import me.jakejmattson.discordkt.api.dsl.precondition

fun permissionPrecondition(permissionService: PermissionService, persistentData: PersistentData) = precondition {
    val command = command ?: return@precondition

    if (guild == null) {
        if (permissionService.hasClearance(null, author, command.requiredPermissionLevel)) {
            return@precondition
        } else {
            return@precondition fail(Messages.INSUFFICIENT_PERMS)
        }
    } else {
        val guild = guild!!
        val member = author.asMember(guild.id)

        val botChannel = persistentData.getGuildProperty(guild) { botChannel }
        if (botChannel.isNotEmpty()
                && channel.id.value != botChannel
                && permissionService.getPermissionLevel(member) > PermissionLevel.Administrator)
            return@precondition fail()

        val level = permissionService.getCommandPermissionLevel(guild, command)

        if (!permissionService.hasClearance(guild, author, level))
            return@precondition fail(Messages.INSUFFICIENT_PERMS)

        return@precondition
    }
}