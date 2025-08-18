package dev.httpmarco.polocloud.modules.rest.auth.role

import dev.httpmarco.polocloud.modules.rest.auth.user.permission.Permission

data class Role(
    val id: Int,
    var label: String,
    var hexColor: String,
    val default: Boolean = false,
) : Permission()