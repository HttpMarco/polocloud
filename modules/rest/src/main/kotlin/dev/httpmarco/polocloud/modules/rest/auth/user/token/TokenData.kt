package dev.httpmarco.polocloud.modules.rest.auth.user.token

import java.util.UUID

data class TokenData(
    val userUUID: UUID,
    val ip: String,
    val userAgent: String?,
    var lastActivity: Long,
)