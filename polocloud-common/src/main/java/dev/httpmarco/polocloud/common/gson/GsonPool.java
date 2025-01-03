package dev.httpmarco.polocloud.common.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@UtilityClass
public final class GsonPool {

    public final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public @NotNull Gson newInstance(@NotNull Consumer<GsonBuilder> builderConsumer) {
        var builder = PRETTY_GSON.newBuilder();
        builderConsumer.accept(builder);
        return builder.create();
    }
}
