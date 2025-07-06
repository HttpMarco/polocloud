package dev.httpmarco.polocloud.sdk.java.groups

import com.google.common.util.concurrent.MoreExecutors
import dev.httpmarco.polocloud.v1.proto.GroupProvider
import dev.httpmarco.polocloud.v1.proto.GroupProviderHandlerGrpc
import io.grpc.ManagedChannel
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class GroupProvider(private val channel: ManagedChannel?) {

    private val directExecutor: Executor = MoreExecutors.directExecutor()
    private val groupStub = GroupProviderHandlerGrpc.newBlockingStub(channel)
    private val asyncGroupStub = GroupProviderHandlerGrpc.newFutureStub(channel)

    fun find() : List<Group> {
        return groupStub.find(GroupProvider.FindRequest.newBuilder().build()).groupsList.stream().map { group -> Group(group) }.toList()
    }

    fun find(name: String) : Group? {
        return groupStub.find(GroupProvider.FindRequest.newBuilder().setName(name).build()).groupsList.stream().map { group -> Group(group) }.findFirst().orElse(null)
    }

    fun findAsync(): CompletableFuture<List<Group>> {
        val request = GroupProvider.FindRequest.newBuilder().build()
        val futureResponse = asyncGroupStub.find(request)

        return completableFromGuava(futureResponse) {
            it.groupsList.map { group -> Group(group) }
        }
    }

    fun findAsync(name: String): CompletableFuture<Group?> {
        val request = GroupProvider.FindRequest.newBuilder().setName(name).build()
        val futureResponse = asyncGroupStub.find(request)

        return completableFromGuava(futureResponse) {
            it.groupsList.map { group -> Group(group) }.firstOrNull()
        }
    }

    private fun <T, R> completableFromGuava(guavaFuture: com.google.common.util.concurrent.ListenableFuture<T>, mapper: (T) -> R): CompletableFuture<R> {
        val completable = CompletableFuture<R>()
        guavaFuture.addListener({
            try {
                val result = mapper(guavaFuture.get())
                completable.complete(result)
            } catch (ex: Exception) {
                completable.completeExceptionally(ex)
            }
        }, directExecutor)
        return completable
    }
}