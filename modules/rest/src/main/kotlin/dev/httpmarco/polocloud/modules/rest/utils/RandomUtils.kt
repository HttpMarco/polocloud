package dev.httpmarco.polocloud.modules.rest.utils

import java.security.SecureRandom

fun generateRandom(length: Int = 170): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    val random = SecureRandom()
    return (1..length)
        .map { chars[random.nextInt(chars.length)] }
        .joinToString("")
}