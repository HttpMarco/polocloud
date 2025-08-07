package dev.httpmarco.polocloud.shared.platform

import dev.httpmarco.polocloud.v1.groups.PlatformSnapshot

data class PlatformIndex(val name: String, val version: String) {

    fun toSnapshot(): PlatformSnapshot {
        return PlatformSnapshot.newBuilder()
            .setName(name)
            .setVersion(version)
            .build()
    }

}