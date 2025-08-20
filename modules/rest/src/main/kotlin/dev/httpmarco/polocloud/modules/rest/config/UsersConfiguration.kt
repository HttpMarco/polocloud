package dev.httpmarco.polocloud.modules.rest.config

import dev.httpmarco.polocloud.modules.rest.auth.role.Role
import dev.httpmarco.polocloud.modules.rest.auth.user.User
import dev.httpmarco.polocloud.modules.rest.utils.generateRandom

data class Users(
    val secret: String = generateRandom(),
    val roles: MutableList<Role> = mutableListOf(),
    val users: MutableList<User> = mutableListOf()
) : Config