package dev.httpmarco.polocloud.shared.information

import dev.httpmarco.polocloud.v1.information.AggregateCloudInformationSnapshot

data class AggregateCloudInformation(
    val timestamp: Long,
    val avgCpu: Double,
    val avgRam: Double
) {

    companion object {
        fun bindSnapshot(snapshot: AggregateCloudInformationSnapshot): AggregateCloudInformation {
            return AggregateCloudInformation(
                snapshot.timestamp,
                snapshot.avgCpu,
                snapshot.avgRam
            )
        }
    }

    fun toSnapshot(): AggregateCloudInformationSnapshot {
        return AggregateCloudInformationSnapshot.newBuilder()
            .setTimestamp(timestamp)
            .setAvgCpu(avgCpu)
            .setAvgRam(avgRam)
            .build()
    }

}