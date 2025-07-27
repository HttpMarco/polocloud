package dev.httpmarco.polocloud.sdk.kotlin.groups

import dev.httpmarco.polocloud.v1.groups.GroupSnapshot

class Group(private val snapshot: GroupSnapshot) {

    fun name(): String = snapshot.name

    fun minimumMemory() = snapshot.minimumMemory

    fun maximumMemory() = snapshot.maximumMemory

    fun minimumOnline() = snapshot.minimumOnline

    fun maximumOnline() = snapshot.maximumOnline

    fun properties() = snapshot.propertiesMap.toMap()

}