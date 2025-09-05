package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command.InspectImageResponse
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.RuntimeFactory
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot


class DockerFactory(val client: DockerClient) : RuntimeFactory<DockerService> {

    override fun bootApplication(service: DockerService) {

        // build image here from template

        if(!existsImage(service.group.platform.toString())) {
            logger.info("Docker image ${service.group.platform} doesn't exist. Building it now...")
        } else {
            logger.info("ups")
        }


        /*
        val containerCmd = client.createContainerCmd("polocloud/TODO:latest")
            .withName(service.name())
            .withExposedPorts(ExposedPort.tcp(service.port))
            // todo use platform args
            .withCmd("java", "-jar", "server.jar")

        val hostConfig = containerCmd.hostConfig!!
        hostConfig.withNetworkMode("polocloud-net")

        if (service.group.platform().type == GroupType.PROXY) {
            hostConfig.withPortBindings(Ports(ExposedPort.tcp(service.port), Ports.Binding.bindPort(service.port)))
        }

        service.containerId = containerCmd.exec().id

         */
    }

    override fun shutdownApplication(service: DockerService, shutdownCleanUp: Boolean): ServiceSnapshot {
        client.stopContainerCmd(service.containerId).exec()
        client.removeContainerCmd(service.containerId).withForce(true).exec()
        return service.toSnapshot()
    }

    override fun generateInstance(group: AbstractGroup): DockerService {
        return DockerService(group)
    }

    fun existsImage(platform: String) : Boolean {
        try {
            val response: InspectImageResponse = client.inspectImageCmd("platform:$platform").exec()
            return true
        } catch (e: Exception) {
            return false
        }
    }

}