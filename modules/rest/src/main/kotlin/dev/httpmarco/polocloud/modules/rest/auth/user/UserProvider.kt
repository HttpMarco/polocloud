package dev.httpmarco.polocloud.modules.rest.auth.user

import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.EncryptionUtil
import dev.httpmarco.polocloud.modules.rest.auth.role.Role
import dev.httpmarco.polocloud.modules.rest.auth.user.token.Token
import dev.httpmarco.polocloud.modules.rest.auth.user.token.TokenData
import dev.httpmarco.polocloud.modules.rest.usersConfiguration
import java.util.UUID

class UserProvider {

    fun login(username: String, password: String, ip: String, userAgent: String?): Token? {
        val user = users().firstOrNull { it.username == username } ?: return null
        if (!EncryptionUtil.verify(user.passwordHash, password)) {
            return null
        }

        val tokenData = TokenData(user.uuid, ip, userAgent, System.currentTimeMillis())
        val generatedToken = generateToken(tokenData)

        val token = Token(generatedToken, tokenData)

        user.tokens.add(token)
        saveUsers()

        return token
    }

    fun logout(user: User, token: Token) {
        user.tokens.remove(token)
        saveUsers()
    }

    fun create(user: User, ip: String, userAgent: String?): Token? {
        val currentUsers = users()
        if (currentUsers.any { it.username == user.username }) {
            return null
        }

        if (currentUsers.isEmpty()) {
            user.role = roles().first { it.id == -1 }
        }

        if (user.role == null) {
            user.role = roles().first { it.id == 0 }
        }

        val tokenData = TokenData(user.uuid, ip, userAgent, System.currentTimeMillis())
        val generatedToken = generateToken(tokenData)

        val token = Token(generatedToken, tokenData)

        user.tokens.add(token)
        currentUsers.add(user)

        saveUsers()
        return token
    }

    fun edit(user: User): User? {
        val currentUsers = users()
        val existingUser = currentUsers.firstOrNull { it.uuid == user.uuid } ?: return null

        existingUser.username = user.username
        existingUser.passwordHash = user.passwordHash
        existingUser.role = user.role

        saveUsers()
        return existingUser
    }

    fun updateActivity(user: User, token: Token) {
        val storedTokens = user.tokens.find { it.value == token.value }
        if (storedTokens == null) {
            return
        }

        storedTokens.data.lastActivity = System.currentTimeMillis()
        saveUsers()
    }

    fun userByUUID(uuid: UUID): User? = users().byUUID(uuid)

    fun users(): MutableList<User> = usersConfiguration.users

    private fun roles(): MutableList<Role> = usersConfiguration.roles

    private fun saveUsers() {
        usersConfiguration.save("local/modules/rest/users")
    }

    private fun generateToken(data: TokenData): String = RestModule.instance.jwtProvider.provider().generateToken(data)

}

private fun List<User>.byUUID(uuid: UUID): User? = firstOrNull { it.uuid == uuid }