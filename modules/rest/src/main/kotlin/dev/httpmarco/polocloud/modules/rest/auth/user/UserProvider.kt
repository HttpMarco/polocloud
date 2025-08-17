package dev.httpmarco.polocloud.modules.rest.auth.user

import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.EncryptionUtil
import dev.httpmarco.polocloud.modules.rest.auth.role.Role
import dev.httpmarco.polocloud.modules.rest.auth.user.token.Token
import dev.httpmarco.polocloud.modules.rest.auth.user.token.TokenData
import dev.httpmarco.polocloud.modules.rest.usersConfiguration
import dev.httpmarco.polocloud.modules.rest.utils.generateRandom
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

    fun create(username: String, role: Role): String? {
        if (users().any { it.username == username }) {
            return null
        }

        val password = generateRandom(12)
        val hashedPassword = EncryptionUtil.encrypt(password)

        val user = User(
            uuid = UUID.randomUUID(),
            username = username,
            passwordHash = hashedPassword,
            role = role,
            createdAt = System.currentTimeMillis(),
            tokens = mutableListOf()
        )

        users().add(user)
        saveUsers()
        return password
    }

    fun createSelf(user: User, ip: String, userAgent: String?): Token? {
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
        existingUser.hasChangedPassword = user.hasChangedPassword
        existingUser.role = user.role

        saveUsers()
        return existingUser
    }

    fun delete(uuid: UUID): Boolean {
        val currentUsers = users()
        val existingUser = currentUsers.firstOrNull { it.uuid == uuid } ?: return false

        currentUsers.remove(existingUser)
        saveUsers()
        return true
    }

    fun deleteToken(user: User, token: Token): Boolean {
        val storedToken = user.tokens.firstOrNull { it.value == token.value } ?: return false
        user.tokens.remove(storedToken)
        saveUsers()
        return true
    }

    fun deleteAllTokens(user: User): Boolean {
        user.tokens.clear()
        saveUsers()
        return true
    }

    fun updateActivity(user: User, token: Token) {
        val storedTokens = user.tokens.find { it.value == token.value }
        if (storedTokens == null) {
            return
        }

        storedTokens.data.lastActivity = System.currentTimeMillis()
        saveUsers()
    }

    fun roleCount(roleId: Int): Int = users().count { it.role?.id == roleId }

    fun userByUUID(uuid: UUID): User? = users().byUUID(uuid)

    fun users(): MutableList<User> = usersConfiguration.users

    private fun roles(): MutableList<Role> = usersConfiguration.roles

    private fun saveUsers() {
        usersConfiguration.save("local/modules/rest/users")
    }

    private fun generateToken(data: TokenData): String = RestModule.instance.jwtProvider.provider().generateToken(data)

}

private fun List<User>.byUUID(uuid: UUID): User? = firstOrNull { it.uuid == uuid }