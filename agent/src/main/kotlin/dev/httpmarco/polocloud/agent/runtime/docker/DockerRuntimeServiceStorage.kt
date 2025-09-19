package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.runtime.RuntimeServiceStorage
import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.common.json.GSON
import dev.httpmarco.polocloud.shared.groups.Group
import dev.httpmarco.polocloud.shared.service.SharedBootConfiguration
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot
import redis.clients.jedis.Jedis
import java.util.concurrent.CompletableFuture

class DockerRuntimeServiceStorage(val client: DockerClient) : RuntimeServiceStorage<DockerService> {

    private val redis = Jedis("polocloud_redis", 6379)
    private val redisKey = "polocloud_services"

    init {
        redis.connect()
    }

    override fun findAll(): List<DockerService> {
        val values = redis.hvals(redisKey)
        return values.map { GSON.fromJson(it, DockerService::class.java) }
    }

    override fun findAllAsync(): CompletableFuture<List<DockerService>> = CompletableFuture.completedFuture(findAll())

    override fun find(name: String): DockerService? {
        return findAll().firstOrNull { it.name().equals(name, ignoreCase = true) }
    }

    override fun findAsync(name: String): CompletableFuture<DockerService?> =
        CompletableFuture.completedFuture<DockerService?>(find(name))


    override fun findByType(type: GroupType): List<DockerService> {
        return findAll().filter { it.type == type }
    }

    override fun findByTypeAsync(type: GroupType): CompletableFuture<List<DockerService>> {
        return CompletableFuture.completedFuture(findByType(type))
    }

    override fun findByGroup(group: Group): List<DockerService> {
        return findAll().filter { it.groupName.equals(group.name, ignoreCase = true) }
    }

    override fun findByGroupAsync(group: Group): CompletableFuture<List<DockerService>> {
        return CompletableFuture.completedFuture(findByGroup(group))
    }

    override fun findByGroup(group: String): List<DockerService> {
        return findAll().filter { it.groupName.equals(group, ignoreCase = true) }
    }

    override fun findByGroupAsync(group: String): CompletableFuture<List<DockerService>> {
        return CompletableFuture.completedFuture(findByGroup(group))
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
        redis.hset(redisKey, fieldKey(service), GSON.toJson(service))
    }

    override fun dropService(service: DockerService) {
        redis.hdel(redisKey, fieldKey(service))
    }

    private fun fieldKey(service: DockerService) = service.name()

    override fun implementedService(abstractService: AbstractService): DockerService {
        return abstractService as? DockerService
            ?: throw IllegalArgumentException("AbstractService must be of type LocalService")
    }
}