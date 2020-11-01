package dev.hbjy.lategamer.commands.administration

import dev.hbjy.lategamer.locale.Messages
import dev.hbjy.lategamer.services.PermissionLevel
import dev.hbjy.lategamer.services.PermissionService
import dev.hbjy.lategamer.services.requiredPermissionLevel
import me.jakejmattson.discordkt.api.arguments.ChoiceArg
import me.jakejmattson.discordkt.api.arguments.RoleArg
import me.jakejmattson.discordkt.api.dsl.commands
import me.jakejmattson.discordkt.api.extensions.addInlineField

fun permissionsCommands(permissionService: PermissionService) = commands("Permissions") {
    guildCommand("rolepermission") {
        description = "Sets or gets the permission level for a role."
        requiredPermissionLevel = PermissionLevel.Staff

        execute(RoleArg("role").makeNullableOptional(null),
                ChoiceArg("level", "Everyone", "Staff", "Administrator", "GuildOwner")
                    .makeNullableOptional(null)) {

            val (role, level) = args

            if (role == null) {
                // Get permission level for all roles
                val permissions = permissionService.getRolePermissionLevels(guild)
                respond {
                    title = "Permission levels for ${guild.name}"
                    description = "These are the configured role permission levels for this guild."

                    permissions.forEach { (roleName, perm) ->
                        addInlineField(roleName, perm.name)
                    }
                }
                return@execute
            }

            if (level == null) {
                // Get permission level for role
                val permissions = permissionService.getRolePermissionLevel(role)
                respond("The role \"${role.name}\" has permission level \"${(permissions ?: PermissionLevel.Everyone).name}\".")
                return@execute
            }

            if (!permissionService.hasClearance(guild, author.asMember(guild.id), PermissionLevel.Administrator)) {
                respond(Messages.INSUFFICIENT_PERMS)
                return@execute
            }

            val success = permissionService.trySetRolePermission(guild, author.asMember(guild.id), role, PermissionLevel.valueOf(level))

            if (!success) {
                respond(Messages.INSUFFICIENT_PERMS)
                return@execute
            } else {
                respond("Successfully updated permissions for role \"${role.name}\".")
            }
        }
    }
}