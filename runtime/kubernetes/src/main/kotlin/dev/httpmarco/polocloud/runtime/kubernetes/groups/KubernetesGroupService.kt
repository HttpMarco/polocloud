package dev.httpmarco.polocloud.runtime.kubernetes.groups

import dev.httpmarco.polocloud.agent.groups.Group
import dev.httpmarco.polocloud.agent.groups.GroupService

class KubernetesGroupService : GroupService {

    override fun find(name: String): Group? {
        TODO("Not yet implemented")
    }

    override fun find(): List<Group> {
        TODO("Not yet implemented")
    }


}