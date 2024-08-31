package dev.httpmarco.polocloud.node.platforms;

import com.google.gson.*;
import dev.httpmarco.polocloud.node.platforms.patcher.PlatformPatcher;
import dev.httpmarco.polocloud.node.platforms.patcher.PlatformPatcherPool;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Type;

public final class PlatformPatcherTypeAdapter implements JsonSerializer<PlatformPatcher>, JsonDeserializer<PlatformPatcher> {

    @Override
    public PlatformPatcher deserialize(@NotNull JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return PlatformPatcherPool.patcher(json.getAsString());
    }

    @Contract("_, _, _ -> new")
    @Override
    public @NotNull JsonElement serialize(@NotNull PlatformPatcher src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.id());
    }
}
