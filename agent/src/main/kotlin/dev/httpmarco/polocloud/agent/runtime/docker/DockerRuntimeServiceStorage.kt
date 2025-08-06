package dev.httpmarco.polocloud.agent.runtime.docker

import dev.httpmarco.polocloud.agent.runtime.RuntimeServiceStorage
import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.shared.service.SharedBootConfiguration
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot
import java.util.concurrent.CompletableFuture

class DockerRuntimeServiceStorage : RuntimeServiceStorage<DockerService> {

    override fun findAll(): List<DockerService> {
        TODO("Not yet implemented")
    }

    override fun findAllAsync(): CompletableFuture<List<DockerService>> {
        TODO("Not yet implemented")
    }

    override fun find(name: String): DockerService? {
        TODO("Not yet implemented")
    }

    override fun findAsync(name: String): CompletableFuture<DockerService?> {
        TODO("Not yet implemented")
    }

    override fun findByType(type: GroupType): List<DockerService> {
        TODO("Not yet implemented")
    }

    override fun findByTypeAsync(type: GroupType): CompletableFuture<List<DockerService>> {
        TODO("Not yet implemented")
    }

    override fun findByGroup(group: dev.httpmarco.polocloud.shared.groups.Group): List<DockerService> {
        TODO("Not yet implemented")
    }

    override fun findByGroupAsync(group: dev.httpmarco.polocloud.shared.groups.Group): CompletableFuture<List<DockerService>> {
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

    override fun bootInstance(name: String): ServiceSnapshot {
        TODO("Not yet implemented")
    }

    override fun shutdownService(name: String): ServiceSnapshot {
        TODO("Not yet implemented")
    }

    override fun deployService(service: DockerService) {
        TODO("Not yet implemented")
    }

    override fun dropService(service: DockerService) {
        TODO("Not yet implemented")
    }

    override fun implementedService(abstractService: AbstractService): DockerService {
        TODO("Not yet implemented")
    }
}