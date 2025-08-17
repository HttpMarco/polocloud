package dev.httpmarco.polocloud.shared.information

data class AggregateCloudInformation(
    val timestamp: Long,
    val avgCpu: Double,
    val avgRam: Double
)