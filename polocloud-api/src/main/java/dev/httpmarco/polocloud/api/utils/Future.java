package dev.httpmarco.polocloud.api.utils;

public class Future<T> {

    public T now() {
        return null;
    }

    public static <T> Future<T> completedFuture(T value) {
        return new Future<>();
    }

}
