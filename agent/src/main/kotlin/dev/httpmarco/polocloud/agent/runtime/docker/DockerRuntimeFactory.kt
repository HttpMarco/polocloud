package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.ContainerSpec
import com.github.dockerjava.api.model.Mount
import com.github.dockerjava.api.model.MountType
import com.github.dockerjava.api.model.NetworkAttachmentConfig
import com.github.dockerjava.api.model.ServiceModeConfig
import com.github.dockerjava.api.model.ServiceReplicatedModeOptions
import com.github.dockerjava.api.model.ServiceSpec
import com.github.dockerjava.api.model.TaskSpec
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.runtime.abstract.AbstractRuntimeFactory
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot

class DockerRuntimeFactory(val client: DockerClient) : AbstractRuntimeFactory<DockerService>() {

    override fun runRuntimeBoot(service: DockerService) {
        client.pullImageCmd("openjdk").withTag("21-jdk").start().awaitCompletion()

        // ContainerSpec
        val containerSpec = ContainerSpec()
            .withImage("openjdk:21-jdk")
            .withDir("/app")
            .withCommand(languageSpecificBootArguments(service))
            .withMounts(listOf(
                Mount().withType(MountType.BIND)
                    .withSource("C:/Users/nervi/Desktop/123/temp/${service.name()}")
                    .withTarget("/app")
            ))

        // Ports, falls Proxy
        if (service.group.platform().type == GroupType.PROXY) {
           // containerSpec.withExposedPorts(ExposedPort.tcp(service.port))
        }

        val taskSpec = TaskSpec()
            .withContainerSpec(containerSpec)
            .withNetworks(listOf(NetworkAttachmentConfig().withTarget(DOCKER_NETWORK)))

        val serviceSpec = ServiceSpec()
            .withName(service.name())
            .withLabels(mapOf("com.docker.stack.namespace" to "polocloud"))
            .withTaskTemplate(taskSpec)
            .withMode(ServiceModeConfig().withReplicated(ServiceReplicatedModeOptions().withReplicas(1)))

        val createServiceResponse = client.createServiceCmd(serviceSpec).exec()
        service.containerId = createServiceResponse.id
    }

    override fun shutdownApplication(service: DockerService, shutdownCleanUp: Boolean): ServiceSnapshot {
        client.stopContainerCmd(service.containerId).exec()
        client.removeContainerCmd(service.containerId).withForce(true).exec()
        return service.toSnapshot()
    }


    override fun generateInstance(group: AbstractGroup): DockerService {
        return DockerService(group)
    }

    override fun javaLanguagePath(service: DockerService): String {
        return "java"
    }
}