package dev.httpmarco.polocloud.shared.service

import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot

class Service(val serviceSnapshot: ServiceSnapshot) {

    fun name(): String {
        return "${serviceSnapshot.groupName}-${serviceSnapshot.id}"
    }

    fun type(): GroupType {
        return serviceSnapshot.serverType
    }

    fun properties(): Map<String, String> {
        return serviceSnapshot.propertiesMap.toMap()
    }

    fun hostname(): String {
        return serviceSnapshot.hostname
    }

    fun port(): Int {
        return serviceSnapshot.port
    }
}