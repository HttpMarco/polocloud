package dev.httpmarco.polocloud.shared.platform

import dev.httpmarco.polocloud.v1.platform.PlatformVersionSnapshot

data class PlatformVersion(
    val version: String
) {

    companion object {
        fun bindSnapshot(snapshot: PlatformVersionSnapshot): PlatformVersion {
            return PlatformVersion(
                snapshot.version
            )
        }
    }

    fun toSnapshot(): PlatformVersionSnapshot {
        return PlatformVersionSnapshot.newBuilder()
            .setVersion(version)
            .build()
    }

}