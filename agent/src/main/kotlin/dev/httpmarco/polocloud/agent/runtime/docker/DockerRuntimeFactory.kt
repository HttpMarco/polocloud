package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.runtime.RuntimeFactory
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot

/**
 * DockerRuntimeFactory is responsible for managing Docker-based service instances.
 * It can start, stop, and generate Docker services dynamically using Docker Swarm.
 * This class is Swarm-aware: it creates services instead of plain containers, allowing
 * replication and overlay networking.
 */
class DockerRuntimeFactory(val client: DockerClient) : RuntimeFactory<DockerService>{

    /**
     * Starts a Docker service using Docker Swarm.
     * Pulls the image, sets up mounts, networking, and service replication.
     */
    override fun bootApplication(service: DockerService) {

    }

    /**
     * Stops and removes the Docker service.
     * Uses the service container ID, forces removal if needed.
     */
    override fun shutdownApplication(service: DockerService, shutdownCleanUp: Boolean): ServiceSnapshot {
        // Stop and remove the container/service
        client.stopContainerCmd(service.containerId).exec()
        client.removeContainerCmd(service.containerId).withForce(true).exec()
        return service.toSnapshot()
    }

    /**
     * Generates a new DockerService instance for a given group.
     */
    override fun generateInstance(group: AbstractGroup): DockerService {
        return DockerService(group)
    }
}
