package dev.httpmarco.polocloud.api.protocol;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CloudResultFuture<T> {

    public T now() {
        return null;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull CloudResultFuture<T> completedFuture(T value) {
        return new CloudResultFuture<>();
    }
}
