package dev.httpmarco.polocloud.shared.groups

import dev.httpmarco.polocloud.v1.groups.GroupInformationSnapshot

open class GroupInformation(val createdAt: Long) {

    companion object {
        fun bindSnapshot(snapshot: GroupInformationSnapshot): GroupInformation{
            return GroupInformation(
                snapshot.createdAt
            )
        }
    }

    fun toSnapshot(): GroupInformationSnapshot {
        return GroupInformationSnapshot.newBuilder()
            .setCreatedAt(this.createdAt)
            .build()
    }

}