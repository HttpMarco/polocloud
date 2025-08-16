package dev.httpmarco.polocloud.common.os

import com.sun.management.OperatingSystemMXBean
import dev.httpmarco.polocloud.common.math.convertBytesToMegabytes
import java.lang.management.ManagementFactory
import kotlin.math.roundToInt

private val osBean: OperatingSystemMXBean by lazy {  ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean }

fun cpuUsage(): Double {
    val load = osBean.cpuLoad
    if (load < 0) {
        return -1.0
    }

    return (load * 10000.0).roundToInt() / 100.0
}

fun usedMemory(): Double {
    val runtime = Runtime.getRuntime()
    val usedBytes = runtime.totalMemory() - runtime.freeMemory()

    return convertBytesToMegabytes(usedBytes)
}

fun maxMemory(): Double {
    val runtime = Runtime.getRuntime()
    val maxBytes = runtime.maxMemory()

    return convertBytesToMegabytes(maxBytes)
}

