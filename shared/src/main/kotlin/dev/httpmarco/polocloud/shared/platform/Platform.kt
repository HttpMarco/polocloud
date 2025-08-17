package dev.httpmarco.polocloud.shared.platform

import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.platform.PlatformSnapshot

open class Platform(
    val name: String,
    val type: GroupType,
    val versions: List<PlatformVersion>
) {

    companion object {
        fun bindSnapshot(snapshot: PlatformSnapshot): Platform {
            return Platform(
                snapshot.name,
                snapshot.type,
                snapshot.versionsList.map { PlatformVersion.bindSnapshot(it) }
            )
        }
    }

    fun toSnapshot(): PlatformSnapshot {
        return PlatformSnapshot.newBuilder()
            .setName(name)
            .setType(type)
            .addAllVersions(versions.map { it.toSnapshot() })
            .build()
    }

}