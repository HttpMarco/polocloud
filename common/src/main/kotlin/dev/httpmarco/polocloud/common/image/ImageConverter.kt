package dev.httpmarco.polocloud.common.image

import java.io.InputStream
import java.util.*

fun pngToBase64DataUrl(stream: InputStream): String {
    val bytes = stream.readBytes()
    val base64 = Base64.getEncoder().encodeToString(bytes)
    return "data:image/png;base64,$base64"
}