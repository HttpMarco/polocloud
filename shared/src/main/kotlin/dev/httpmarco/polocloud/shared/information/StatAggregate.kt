package dev.httpmarco.polocloud.shared.information

data class StatAggregate(
    var cpuSum: Double = 0.0,
    var ramSum: Double = 0.0,
    var count: Int = 0
) {
    val avgCpu get() = if (count > 0) cpuSum / count else 0.0
    val avgRam get() = if (count > 0) ramSum / count else 0.0
}