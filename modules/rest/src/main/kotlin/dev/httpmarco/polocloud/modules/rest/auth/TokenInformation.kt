package dev.httpmarco.polocloud.modules.rest.auth

import java.util.UUID

data class TokenInformation(
    val userUUID: UUID,
    val ip: String,
    val userAgent: String?,
)