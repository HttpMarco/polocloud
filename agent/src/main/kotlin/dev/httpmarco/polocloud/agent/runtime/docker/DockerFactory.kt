package dev.httpmarco.polocloud.agent.runtime.docker

import dev.httpmarco.polocloud.agent.runtime.RuntimeFactory
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot

class DockerFactory : RuntimeFactory<DockerService> {
    override fun bootApplication(service: DockerService) {
        TODO("Not yet implemented")
    }

    override fun shutdownApplication(service: DockerService, shutdownCleanUp: Boolean): ServiceSnapshot {
        TODO("Not yet implemented")
    }
}