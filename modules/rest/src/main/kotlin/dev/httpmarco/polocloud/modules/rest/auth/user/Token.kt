package dev.httpmarco.polocloud.modules.rest.auth.user

data class Token(
    val value: String,
    val data: TokenData
)