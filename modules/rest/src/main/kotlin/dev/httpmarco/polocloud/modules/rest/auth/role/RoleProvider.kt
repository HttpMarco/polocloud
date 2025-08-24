package dev.httpmarco.polocloud.modules.rest.auth.role

import dev.httpmarco.polocloud.modules.rest.usersConfiguration

class RoleProvider {

    init {
        defaultRoles()
    }

    fun createRole(label: String, hexColor: String, permissions: List<String>): Role? {
        if (roles().any { it.label.equals(label, ignoreCase = true) }) {
            return null
        }

        val nextId = (roles().filter { it.id > 0 }.maxOfOrNull { it.id } ?: 0) + 1

        val role = Role(
            id = nextId,
            label = label,
            hexColor = hexColor
        )

        permissions.forEach { role.addPermission(it) }

        roles().add(role)
        saveRoles()

        return role
    }

    fun deleteRole(role: Role): Boolean {
        if (role.default) {
            return false
        }

        val currentRoles = roles()
        if (currentRoles.removeIf { it.id == role.id }) {
            saveRoles()
            return true
        }

        return false
    }

    fun editRole(role: Role): Boolean {
        val currentRoles = roles()
        val index = currentRoles.indexOfFirst { it.id == role.id }

        if (index == -1) {
            return false
        }

        currentRoles[index] = role
        saveRoles()

        return true
    }

    fun roleById(id: Int): Role? = roles().firstOrNull { it.id == id }

    fun roles(): MutableList<Role> = usersConfiguration.roles

    private fun saveRoles() {
        usersConfiguration.save("local/modules/rest/users")
    }

    private fun defaultRoles() {
        val currentRoles = roles()

        if (currentRoles.none { it.id == -1 }) {
            currentRoles.add(Role(-1, "Admin", "#FF0000", true).apply {
                addPermission("*")
            })
        }

        if (currentRoles.none { it.id == 0 }) {
            currentRoles.add(Role(0,"User", "#00FF00", true).apply {
                addPermissions(
                    //viewing permissions
                    "polocloud.system.version",
                    "polocloud.system.information",
                    "polocloud.user.list",
                    "polocloud.user.get",
                    "polocloud.service.count",
                    "polocloud.service.list",
                    "polocloud.role.list",
                    "polocloud.role.get",
                    "polocloud.player.get",
                    "polocloud.players.list",
                    "polocloud.platform.list",
                    "polocloud.group.count",
                    "polocloud.group.list",
                    "polocloud.group.get",
                    "polocloud.templates.list",

                    //websocket
                    "polocloud.ws.alive",
                    "polocloud.ws.logs",
                    "polocloud.service.screen",

                    // user self permissions
                    "polocloud.user.self.edit",
                    "polocloud.user.self.change-password",
                    "polocloud.user.self.tokens",
                    "polocloud.user.self.token.delete"
                )
            })
        }

        saveRoles()
    }
}