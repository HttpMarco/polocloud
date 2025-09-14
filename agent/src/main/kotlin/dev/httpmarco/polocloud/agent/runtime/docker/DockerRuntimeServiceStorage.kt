package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.RuntimeServiceStorage
import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.shared.groups.Group
import dev.httpmarco.polocloud.shared.service.SharedBootConfiguration
import dev.httpmarco.polocloud.shared.template.Template
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot
import dev.httpmarco.polocloud.v1.services.ServiceState
import java.util.concurrent.CompletableFuture

class DockerRuntimeServiceStorage(val client: DockerClient) : RuntimeServiceStorage<DockerService> {

    override fun findAll(): List<DockerService> {
        val services = client.listServicesCmd().exec()
        val result = arrayListOf<DockerService>()

        services.forEach { service ->
            val spec = service.spec ?: return@forEach

            // nur Services mit Label "polocloud"
            if (spec.labels?.containsKey("polocloud") != true) {
                return@forEach
            }

            val serviceName = spec.name
            val labels = spec.labels

            val tasks = client.listTasksCmd()
                .withServiceFilter(service.id)
                .exec()

            val firstTask = tasks.firstOrNull()
            val containerId = firstTask?.status?.containerStatus?.containerID

            val hostname = if (containerId != null) {
                val containerInfo = client.inspectContainerCmd(containerId).exec()
                containerInfo.config?.hostName ?: "unknown"
            } else {
                "unknown"
            }

            result.add(
                DockerService(
                    name = serviceName.split("-")[1],
                    index = firstTask?.slot ?: -1,
                    state = ServiceState.PREPARING,
                    platformType = GroupType.PROXY,
                    environment = hashMapOf(),
                    host = hostname,
                    port = 25565,
                    templates = listOf(),
                    information = dev.httpmarco.polocloud.shared.service.ServiceInformation(System.currentTimeMillis()),
                    minMemory = labels?.get("minMemory")?.toIntOrNull() ?: 512,
                    maxMemory = labels?.get("maxMemory")?.toIntOrNull() ?: 1024
                )
            )
        }
        return result
    }

    override fun findAllAsync() = CompletableFuture.completedFuture(findAll())

    override fun find(name: String): DockerService? {
        return null;
    }

    override fun findAsync(name: String): CompletableFuture<DockerService?> =
        CompletableFuture.completedFuture<DockerService?>(find(name))


    override fun findByType(type: GroupType): List<DockerService> {
        TODO("Not yet implemented")
    }

    override fun findByTypeAsync(type: GroupType): CompletableFuture<List<DockerService>> {
        TODO("Not yet implemented")
    }

    override fun findByGroup(group: Group): List<DockerService> {
        return listOf()
    }

    override fun findByGroupAsync(group: Group): CompletableFuture<List<DockerService>> {
        TODO("Not yet implemented")
    }

    override fun findByGroup(group: String): List<DockerService> {
        TODO("Not yet implemented")
    }

    override fun findByGroupAsync(group: String): CompletableFuture<List<DockerService>> {
        TODO("Not yet implemented")
    }

    override fun bootInstanceWithConfiguration(
        name: String,
        configuration: SharedBootConfiguration
    ): ServiceSnapshot {
        TODO("Not yet implemented")
    }

    override fun shutdownService(name: String): ServiceSnapshot {
        return Agent.runtime.factory()
            .shutdownApplication(find(name) ?: throw IllegalArgumentException("Service not found: $name"))
    }

    override fun deployService(service: DockerService) {

    }

    override fun dropService(service: DockerService) {

    }

    override fun implementedService(abstractService: AbstractService): DockerService {
        return abstractService as? DockerService
            ?: throw IllegalArgumentException("AbstractService must be of type LocalService")
    }
}