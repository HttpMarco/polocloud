package dev.httpmarco.polocloud.agent.runtime.docker

import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.services.AbstractService

class DockerService(abstractGroup: AbstractGroup) : AbstractService(abstractGroup) {

    lateinit var containerId : String


}