package dev.httpmarco.polocloud.shared.platform

import dev.httpmarco.polocloud.v1.groups.GroupPlatformSnapshot

data class PlatformIndex(val name: String, val version: String) {

    fun toSnapshot(): GroupPlatformSnapshot {
        return GroupPlatformSnapshot.newBuilder()
            .setName(name)
            .setVersion(version)
            .build()
    }

    override fun toString(): String {
        return "$name-$version"
    }
}