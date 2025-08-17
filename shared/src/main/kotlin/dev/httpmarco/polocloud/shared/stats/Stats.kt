package dev.httpmarco.polocloud.shared.stats

import dev.httpmarco.polocloud.v1.stats.StatsSnapshot

open class Stats(
    val started: Long,
    val runtime: String,
    val javaVersion: String,
    val cpuUsage: Double,
    val usedMemory: Double,
    val maxMemory: Double,
    val subscribedEvents: Int
) {

    companion object {
        fun bindSnapshot(snapshot: StatsSnapshot): Stats {
            return Stats(
                snapshot.started,
                snapshot.runtime,
                snapshot.javaVersion,
                snapshot.cpuUsage,
                snapshot.usedMemory,
                snapshot.maxMemory,
                snapshot.subscribedEvents
            )
        }
    }

    fun toSnapshot(): StatsSnapshot {
        return StatsSnapshot.newBuilder()
            .setStarted(started)
            .setRuntime(runtime)
            .setJavaVersion(javaVersion)
            .setCpuUsage(cpuUsage)
            .setUsedMemory(usedMemory)
            .setMaxMemory(maxMemory)
            .setSubscribedEvents(subscribedEvents)
            .build()
    }

    override fun equals(other: Any?): Boolean =
        other is Stats &&
                started == other.started &&
                runtime == other.runtime &&
                javaVersion == other.javaVersion &&
                cpuUsage == other.cpuUsage &&
                usedMemory == other.usedMemory &&
                maxMemory == other.maxMemory &&
                subscribedEvents == other.subscribedEvents

    override fun hashCode(): Int =
        listOf(started, runtime, javaVersion, cpuUsage, usedMemory, maxMemory, subscribedEvents).hashCode()
}