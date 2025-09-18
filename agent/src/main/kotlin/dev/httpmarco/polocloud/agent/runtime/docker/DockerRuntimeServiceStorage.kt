package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.runtime.RuntimeServiceStorage
import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.shared.groups.Group
import dev.httpmarco.polocloud.shared.service.SharedBootConfiguration
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot
import redis.clients.jedis.Jedis
import java.util.concurrent.CompletableFuture

class DockerRuntimeServiceStorage(val client: DockerClient) : RuntimeServiceStorage<DockerService> {

    private val redis = Jedis("polocloud_redis", 6379);

    init {
        redis.connect()
    }


    override fun findAll(): List<DockerService> {
        return listOf()
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