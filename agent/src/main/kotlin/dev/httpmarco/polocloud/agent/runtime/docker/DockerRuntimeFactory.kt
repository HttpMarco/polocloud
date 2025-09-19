package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.Mount
import com.github.dockerjava.api.model.MountType
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.runtime.abstract.AbstractRuntimeFactory
import dev.httpmarco.polocloud.v1.GroupType
import kotlin.io.path.Path

/**
 * DockerRuntimeFactory is responsible for managing Docker-based service instances.
 * It can start, stop, and generate Docker services dynamically using Docker Swarm.
 * This class is Swarm-aware: it creates services instead of plain containers, allowing
 * replication and overlay networking.
 */
class DockerRuntimeFactory(val client: DockerClient) : AbstractRuntimeFactory<DockerService>(Path("local/temp")) {

    /**
     * Starts a Docker service using Docker Swarm.
     * Pulls the image, sets up mounts, networking, and service replication.
     */
    override fun runRuntimeBoot(service: DockerService) {
        val rawContainer = client.createContainerCmd("openjdk:21-jdk")
            .withName(service.name())
            .withWorkingDir("/app")
            .withCmd(*languageSpecificBootArguments(service).toTypedArray())

        val hostConfig = rawContainer.hostConfig!!
            .withMounts(
                listOf(
                    Mount().withType(MountType.BIND)
                        .withSource("C:\\Users\\mirco\\Desktop\\te\\temp\\${service.name()}")
                        .withTarget("/app")
                )
            )


            if(service.type == GroupType.PROXY) {
                val exposed = ExposedPort.tcp(service.port)
                rawContainer.withExposedPorts(exposed)
                hostConfig.withPortBindings(PortBinding(Ports.Binding.bindPort(service.port),exposed))
            }

        val container = rawContainer.exec()
        client.startContainerCmd(container.id).exec()

        val inspection = client.inspectContainerCmd(container.id).exec()
        val ip = inspection.networkSettings.networks.values.firstOrNull()?.ipAddress!!

        service.containerId = container.id
        service.changeToContainerHostname(ip)
    }

    /**
     * Stops and removes the Docker service.
     * Uses the service container ID, forces removal if needed.
     */
    override fun runRuntimeShutdown(service: DockerService, shutdownCleanUp: Boolean) {
        client.stopContainerCmd(service.containerId).exec()
        client.removeContainerCmd(service.containerId).withForce(true).exec()
    }

    override fun javaLanguagePath(service: DockerService): String {
        return "java"
    }

    /**
     * Generates a new DockerService instance for a given group.
     */
    override fun generateInstance(group: AbstractGroup): DockerService {
        return DockerService(group)
    }
}
