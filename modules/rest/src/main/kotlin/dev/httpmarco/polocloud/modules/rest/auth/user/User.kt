package dev.httpmarco.polocloud.modules.rest.auth.user

import dev.httpmarco.polocloud.modules.rest.auth.role.Role
import dev.httpmarco.polocloud.modules.rest.auth.user.token.Token
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