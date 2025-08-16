package dev.httpmarco.polocloud.modules.rest.auth.user

import dev.httpmarco.polocloud.modules.rest.auth.user.permission.Permission
import java.util.UUID

data class User(
    val uuid: UUID,
    var username: String,
    var passwordHash: String,
    val createdAt: Long,
    val tokens: MutableList<Token> = mutableListOf(),
) : Permission()