package dev.httpmarco.polocloud.modules.rest.config

import dev.httpmarco.polocloud.modules.rest.auth.role.Role
import dev.httpmarco.polocloud.modules.rest.auth.user.User
import java.security.SecureRandom

data class Users(
    val secret: String = generateJwtSecret(),
    val roles: MutableList<Role> = mutableListOf(),
    val users: MutableList<User> = mutableListOf()
) : Config

fun generateJwtSecret(length: Int = 170): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val random = SecureRandom()
    return (1..length)
        .map { chars[random.nextInt(chars.length)] }
        .joinToString("")
}