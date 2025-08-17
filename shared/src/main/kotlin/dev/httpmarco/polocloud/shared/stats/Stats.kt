package dev.httpmarco.polocloud.shared.stats

import dev.httpmarco.polocloud.shared.polocloudShared
import dev.httpmarco.polocloud.v1.groups.StatsSnapshot

open class Stats(
    val started: Long,
    val cpuUsage: Double,
    val usedMemory: Double
) {

    companion object {
        fun bindSnapshot(snapshot: StatsSnapshot): Stats {
            return Stats(
                snapshot.started,
                snapshot.cpuUsage,
                snapshot.usedMemory
            )
        }
    }

    fun toSnapshot(): StatsSnapshot {
        return StatsSnapshot.newBuilder()
            .setStarted(started)
            .setCpuUsage(cpuUsage)
            .setUsedMemory(usedMemory)
            .build()
    }

    override fun equals(other: Any?): Boolean =
        other is Stats &&
                started == other.started &&
                cpuUsage == other.cpuUsage &&
                usedMemory == other.usedMemory

    override fun hashCode(): Int =
        listOf(started, cpuUsage, usedMemory).hashCode()
}