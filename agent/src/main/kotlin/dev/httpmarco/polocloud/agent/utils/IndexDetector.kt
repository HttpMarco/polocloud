package dev.httpmarco.polocloud.agent.utils

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.AbstractGroup

class IndexDetector {

    companion object {
        fun findIndex(group: AbstractGroup): Int {
            var id = 1
            while (Agent.runtime.serviceStorage().findAll().stream()
                    .anyMatch { it.groupName == group.name && it.id == id }
            ) {
                id++
            }
            return id
        }
    }

}