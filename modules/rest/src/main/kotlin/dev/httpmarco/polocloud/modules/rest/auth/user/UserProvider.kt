package dev.httpmarco.polocloud.modules.rest.auth.user

import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.TokenInformation
import dev.httpmarco.polocloud.modules.rest.usersConfiguration
import java.util.UUID

class UserProvider {

    fun create(user: User, ip: String, userAgent: String?): String? {
        val currentUsers = users()
        if (currentUsers.any { it.username == user.username }) {
            return null
        }

        if (currentUsers.isEmpty()) {
            user.addPermission("*")
        }

        currentUsers.add(user)
        saveUsers()

        return generateToken(user, ip, userAgent)
    }

    fun userByUUID(uuid: UUID): User? = users().byUUID(uuid)

    private fun users(): MutableList<User> = usersConfiguration.users

    private fun saveUsers() {
        usersConfiguration.save("local/modules/rest/users")
    }

    private fun generateToken(user: User, ip: String, userAgent: String?): String? = RestModule.instance.jwtProvider.provider().generateToken(TokenInformation(user.uuid, ip, userAgent))
}

private fun List<User>.byUUID(uuid: UUID): User? = firstOrNull { it.uuid == uuid }