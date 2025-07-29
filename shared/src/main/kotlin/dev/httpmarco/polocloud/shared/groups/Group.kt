package dev.httpmarco.polocloud.shared.groups

import dev.httpmarco.polocloud.v1.groups.GroupSnapshot

class Group(val groupSnapshot: GroupSnapshot) {

    fun name(): String {
        return groupSnapshot.name
    }

}