package dev.httpmarco.polocloud.common.math

import java.math.BigDecimal
import java.math.RoundingMode

fun convertBytesToMegabytes(bytes: Long): Double {
   return BigDecimal(bytes / (1024 * 1024))
        .setScale(2, RoundingMode.HALF_UP)
        .toDouble()
}