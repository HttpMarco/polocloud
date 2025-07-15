package dev.httpmarco.polocloud.sdk.java.services

import dev.httpmarco.polocloud.v1.GroupType

class Service(val groupName: String, val id: Int, val hostname: String, val port: Int, val type : GroupType) {

    fun name(): String {
        return "${groupName}-$id"
    }

}