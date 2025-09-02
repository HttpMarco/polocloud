package dev.httpmarco.polocloud.modules.rest.auth.user

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.auth.role.Role
import dev.httpmarco.polocloud.modules.rest.auth.user.token.Token
import dev.httpmarco.polocloud.modules.rest.auth.user.token.toJson
import java.util.UUID

data class User(
    val uuid: UUID,
    var username: String,
    var role: Role?,
    var passwordHash: String,
    var hasChangedPassword: Boolean = false,
    val createdAt: Long,
    val tokens: MutableList<Token> = mutableListOf(),
)

fun User.toJson(includeTokens: Boolean = false): JsonObject {
    return JsonObject().apply {
        addProperty("uuid", uuid.toString())
        addProperty("username", username)
        addProperty("createdAt", createdAt)
        addProperty("hasChangedPassword", hasChangedPassword)
        addProperty("role", role?.id ?: 0)

        if (includeTokens) {
            val tokensArray = JsonArray()
            tokens.forEach { token ->
                tokensArray.add(token.toJson())
            }
            add("tokens", tokensArray)
        }
    }
}