package dev.httpmarco.polocloud.modules.rest.auth.user.token

data class Token(
    val value: String,
    val data: TokenData
)