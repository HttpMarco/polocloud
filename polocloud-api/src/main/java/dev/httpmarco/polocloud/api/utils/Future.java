package dev.httpmarco.polocloud.api.utils;

import lombok.AllArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@AllArgsConstructor
public class Future<T> {

    private CompletableFuture<T> future;

    public T now() {
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Future<T> completedFuture(T value) {
        return new Future<>(CompletableFuture.completedFuture(value));
    }
}
