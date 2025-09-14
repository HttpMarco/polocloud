package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.runtime.RuntimeServiceStorage
import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.shared.groups.Group
import dev.httpmarco.polocloud.shared.service.SharedBootConfiguration
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
            if (spec.labels?.containsKey("polocloud") != true) return@forEach

            val serviceName = spec.name
            val tasks = client.listTasksCmd()
                .withServiceFilter(service.id)
                .exec()

            tasks.filter {
                return@filter it.status.state.name == "RUNNING"
            }.forEach { task ->
                val serviceInfo = client.inspectServiceCmd(service.id).exec()
                val virtualIp = serviceInfo.endpoint?.virtualIPs?.firstOrNull()?.addr?.substringBefore('/') ?: "unknown"
                val labels = task.spec.containerSpec!!.labels!!

                result.add(
                    DockerService(
                        name = serviceName.split("-").getOrElse(1) { serviceName },
                        index = task.slot ?: -1,
                        state = ServiceState.valueOf(labels["state"]!!),
                        platformType = GroupType.valueOf(labels["type"] ?: GroupType.UNRECOGNIZED.name),
                        environment = hashMapOf(),
                        host = virtualIp,
                        port = 25565,
                        templates = listOf(),
                        information = dev.httpmarco.polocloud.shared.service.ServiceInformation(System.currentTimeMillis()),
                        minMemory = labels["minMemory"]!!.toInt(),
                        maxMemory = labels["maxMemory"]!!.toInt()
                    )
                )
            }
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