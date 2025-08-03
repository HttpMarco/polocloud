package dev.httpmarco.polocloud.common.future

import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListeningExecutorService
import com.google.common.util.concurrent.MoreExecutors
import java.util.concurrent.CompletableFuture

private val directExecutor: ListeningExecutorService = MoreExecutors.newDirectExecutorService()

fun <T, R> completableFromGuava(guavaFuture: ListenableFuture<T>, mapper: (T) -> R): CompletableFuture<R> {
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