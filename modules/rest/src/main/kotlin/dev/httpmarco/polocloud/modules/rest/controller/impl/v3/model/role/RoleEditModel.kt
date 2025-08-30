package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.role

import dev.httpmarco.polocloud.modules.rest.auth.user.permission.Permission

data class RoleEditModel(
    val label: String = "",
    val hexColor: String = "",
) : Permission()