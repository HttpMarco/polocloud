package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.user

data class UserSelfCreateModel(
    val username: String = "",
    val password: String = "",
    val roleId: Int = 0,
)