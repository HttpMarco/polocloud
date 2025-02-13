package dev.httpmarco.polocloud.suite.utils.serializer;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.Version;

import java.lang.reflect.Type;

public final class VersionSerializer implements JsonSerializer<Version>, JsonDeserializer<Version> {

    public static VersionSerializer INSTANCE = new VersionSerializer();

    @Override
    public Version deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Version.parse(json.getAsString());
    }

    @Override
    public JsonElement serialize(Version src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
