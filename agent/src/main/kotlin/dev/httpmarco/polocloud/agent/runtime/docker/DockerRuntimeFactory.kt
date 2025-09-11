package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.ContainerSpec
import com.github.dockerjava.api.model.EndpointSpec
import com.github.dockerjava.api.model.Mount
import com.github.dockerjava.api.model.MountType
import com.github.dockerjava.api.model.NetworkAttachmentConfig
import com.github.dockerjava.api.model.PortConfig
import com.github.dockerjava.api.model.ServiceModeConfig
import com.github.dockerjava.api.model.ServiceReplicatedModeOptions
import com.github.dockerjava.api.model.ServiceRestartCondition
import com.github.dockerjava.api.model.ServiceRestartPolicy
import com.github.dockerjava.api.model.ServiceSpec
import com.github.dockerjava.api.model.TaskSpec
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.runtime.abstract.AbstractRuntimeFactory
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot

/**
 * DockerRuntimeFactory is responsible for managing Docker-based service instances.
 * It can start, stop, and generate Docker services dynamically using Docker Swarm.
 * This class is Swarm-aware: it creates services instead of plain containers, allowing
 * replication and overlay networking.
 */
class DockerRuntimeFactory(val client: DockerClient) : AbstractRuntimeFactory<DockerService>(DOCKER_FACTORY_PATH) {

    /**
     * Starts a Docker service using Docker Swarm.
     * Pulls the image, sets up mounts, networking, and service replication.
     */
    override fun runRuntimeBoot(service: DockerService) {
        // Pull the base image before creating the service
        client.pullImageCmd("openjdk").withTag("21-jdk").start().awaitCompletion()

        // Define the container specification for Swarm
        val containerSpec = ContainerSpec()
            .withImage("openjdk:21-jdk")
            .withDir("/app") // Working directory inside the container
            .withCommand(languageSpecificBootArguments(service))
            .withMounts(
                listOf(
                    Mount().withType(MountType.BIND)
                        .withSource("C:/Users/mirco/Desktop/te/temp/${service.name()}")
                        .withTarget("/app") // Bind mount host folder to container
                )
            )

        // Optional: Expose ports if the service is a proxy
        val endpointSpec = if (service.group.platform().type == GroupType.PROXY) {
            // Publish the container's port to the same port on the host
            EndpointSpec().withPorts(
                listOf(
                    PortConfig()
                        .withProtocol(com.github.dockerjava.api.model.PortConfigProtocol.TCP)
                        .withTargetPort(service.port)    // Container port
                        .withPublishedPort(service.port)
                        .withPublishMode(PortConfig.PublishMode.ingress)
                )
            )
        } else null

        // Define the task specification, including network attachment
        val taskSpec = TaskSpec()
            .withContainerSpec(containerSpec)
            .withRestartPolicy(ServiceRestartPolicy().withCondition(ServiceRestartCondition.NONE))
            .withNetworks(listOf(NetworkAttachmentConfig().withTarget(DOCKER_NETWORK))) // Overlay network

        // Define the service specification (name, replication, labels)
        val serviceSpec = ServiceSpec()
            .withName(service.name()) // Swarm will generate actual container names
            .withLabels(mapOf("com.docker.stack.namespace" to "polocloud")) // Stack namespace label
            .withTaskTemplate(taskSpec)
            .withMode(ServiceModeConfig().withReplicated(ServiceReplicatedModeOptions().withReplicas(1)))
            .apply {
                if (endpointSpec != null) this.withEndpointSpec(endpointSpec)
            }

        // Create the service in Docker Swarm
        val createServiceResponse = client.createServiceCmd(serviceSpec).exec()
        service.containerId = createServiceResponse.id // Store service ID
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

    /**
     * Returns the Java executable path for the Docker container.
     */
    override fun javaLanguagePath(service: DockerService): String {
        return "java"
    }
}
