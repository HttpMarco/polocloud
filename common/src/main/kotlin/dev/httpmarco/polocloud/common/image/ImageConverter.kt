package dev.httpmarco.polocloud.common.image

import java.io.File
import java.util.Base64

fun pngToBase64DataUrl(file: File): String {
    val bytes = file.readBytes()
    val base64 = Base64.getEncoder().encodeToString(bytes)
    return "data:image/png;base64,$base64"
}