package dev.httpmarco.polocloud.node.utils.serializer;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.Version;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public final class VersionSerializer implements JsonSerializer<Version>, JsonDeserializer<Version> {

    @Contract("_, _, _ -> new")
    @Override
    public @NotNull Version deserialize(@NotNull JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Version.parse(jsonElement.getAsString());
    }

    @Contract("_, _, _ -> new")
    @Override
    public @NotNull JsonElement serialize(@NotNull Version version, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(version.originalVersion());
    }
}
