package dev.httpmarco.polocloud.agent.runtime.docker

import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.runtime.RuntimeGroupStorage
import java.util.concurrent.CompletableFuture


class DockerRuntimeGroupStorage : RuntimeGroupStorage {

    override fun update(group: AbstractGroup) {
        TODO("Not yet implemented")
    }

    override fun reload() {
        TODO("Not yet implemented")
    }

    override fun destroy(abstractGroup: AbstractGroup) {
        TODO("Not yet implemented")
    }

    override fun publish(abstractGroup: AbstractGroup) {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<AbstractGroup> {
        TODO("Not yet implemented")
    }

    override fun findAllAsync(): CompletableFuture<List<AbstractGroup>> {
        TODO("Not yet implemented")
    }

    override fun find(name: String): AbstractGroup? {
        TODO("Not yet implemented")
    }

    override fun findAsync(name: String): CompletableFuture<AbstractGroup?> {
        TODO("Not yet implemented")
    }
}