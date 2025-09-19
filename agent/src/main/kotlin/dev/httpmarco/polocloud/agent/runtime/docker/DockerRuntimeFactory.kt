package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.Bind
import com.github.dockerjava.api.model.Volume
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.runtime.RuntimeFactory
import dev.httpmarco.polocloud.common.language.Language
import dev.httpmarco.polocloud.common.os.currentOS
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot
import kotlin.io.path.Path
import kotlin.io.path.name

/**
 * DockerRuntimeFactory is responsible for managing Docker-based service instances.
 * It can start, stop, and generate Docker services dynamically using Docker Swarm.
 * This class is Swarm-aware: it creates services instead of plain containers, allowing
 * replication and overlay networking.
 */
class DockerRuntimeFactory(val client: DockerClient) : RuntimeFactory<DockerService> {

    /**
     * Starts a Docker service using Docker Swarm.
     * Pulls the image, sets up mounts, networking, and service replication.
     */
    override fun bootApplication(service: DockerService) {
        val container = client.createContainerCmd("openjdk:21-jdk")
            .withName(service.name())
            .withBinds(
                Bind(
                    Path("C:/Users/mirco/Desktop/te/temp/${service.name()}").toAbsolutePath().toString(),
                    Volume("/app")
                )
            )
            .withWorkingDir("/app")
            .withCmd(*languageSpecificBootArguments(service.group()).toTypedArray())
            .exec()

        client.startContainerCmd(container.id).exec()
        println("Container proxy-${service.name()} gestartet mit ID: ${container.id}")
    }

    protected fun languageSpecificBootArguments(group: AbstractGroup): ArrayList<String> {
        val platform = group.platform()
        val commands = ArrayList<String>()
        when (platform.language) {
            Language.JAVA -> {
                commands.add("java")
                commands.addAll(
                    listOf(
                        "-Dterminal.jline=false",
                        "-Dfile.encoding=UTF-8",
                        "-Xms${group.minMemory}M",
                        "-Xmx${group.maxMemory}M",
                        "-jar",
                        group.applicationPlatformFile().name
                    )
                )
                commands.addAll(platform.arguments)
            }

            Language.GO, Language.RUST -> {
                commands.addAll(currentOS.executableCurrentDirectoryCommand(group.applicationPlatformFile().name))
            }
        }
        return commands
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
