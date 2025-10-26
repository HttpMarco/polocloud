package dev.httpmarco.polocloud.shared.groups

import java.util.concurrent.CompletableFuture

interface SharedGroupProvider<G : Group> {

    fun findAll(): List<G>

    fun findAllAsync(): CompletableFuture<List<G>>

    fun find(name: String): G?

    fun findAsync(name: String): CompletableFuture<G?>

    fun create(group: G): G?

    fun createAsync(group: G): CompletableFuture<G?>

    fun update(group: G): G?

    fun updateAsync(group: G): CompletableFuture<G?>

    fun delete(name: String): G?

    fun delete(group: G) {
        delete(group.name)
    }
}