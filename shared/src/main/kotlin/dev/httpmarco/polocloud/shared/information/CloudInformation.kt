package dev.httpmarco.polocloud.shared.information

import dev.httpmarco.polocloud.v1.information.CloudInformationSnapshot

open class CloudInformation(
    val started: Long,
    val runtime: String,
    val javaVersion: String,
    val cpuUsage: Double,
    val usedMemory: Double,
    val maxMemory: Double,
    val subscribedEvents: Int,
    val timestamp: Long
) {

    companion object {
        fun bindSnapshot(snapshot: CloudInformationSnapshot): CloudInformation {
            return CloudInformation(
                snapshot.started,
                snapshot.runtime,
                snapshot.javaVersion,
                snapshot.cpuUsage,
                snapshot.usedMemory,
                snapshot.maxMemory,
                snapshot.subscribedEvents,
                snapshot.timestamp
            )
        }
    }

    fun toSnapshot(): CloudInformationSnapshot {
        return CloudInformationSnapshot.newBuilder()
            .setStarted(started)
            .setRuntime(runtime)
            .setJavaVersion(javaVersion)
            .setCpuUsage(cpuUsage)
            .setUsedMemory(usedMemory)
            .setMaxMemory(maxMemory)
            .setSubscribedEvents(subscribedEvents)
            .setTimestamp(timestamp)
            .build()
    }

    override fun equals(other: Any?): Boolean =
        other is CloudInformation &&
                started == other.started &&
                runtime == other.runtime &&
                javaVersion == other.javaVersion &&
                cpuUsage == other.cpuUsage &&
                usedMemory == other.usedMemory &&
                maxMemory == other.maxMemory &&
                subscribedEvents == other.subscribedEvents &&
                timestamp == other.timestamp

    override fun hashCode(): Int =
        listOf(started, runtime, javaVersion, cpuUsage, usedMemory, maxMemory, subscribedEvents, timestamp).hashCode()
}