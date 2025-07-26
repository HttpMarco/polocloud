package dev.httpmarco.polocloud.common.image

import java.io.InputStream
import java.util.Base64

fun pngToBase64DataUrl(pngFile: InputStream): String {
    val bytes = pngFile.readBytes()
    val base64 = Base64.getEncoder().encodeToString(bytes)
    return "data:image/png;base64,$base64"
}