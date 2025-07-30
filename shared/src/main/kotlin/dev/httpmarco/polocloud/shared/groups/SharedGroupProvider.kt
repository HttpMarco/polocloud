package dev.httpmarco.polocloud.shared.groups

import java.util.concurrent.CompletableFuture

interface SharedGroupProvider {

    fun find() : List<Group>

    fun findAsync() : CompletableFuture<List<Group>>

    fun find(name: String) : Group?

    fun findAsync(name: String) : CompletableFuture<Group?>

}