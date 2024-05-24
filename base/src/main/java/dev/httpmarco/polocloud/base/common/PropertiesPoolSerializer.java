package dev.httpmarco.polocloud.base.common;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;

import java.lang.reflect.Type;

public final class PropertiesPoolSerializer implements JsonSerializer<PropertiesPool<?>>, JsonDeserializer<PropertiesPool<?>> {

    @Override
    public PropertiesPool<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(PropertiesPool<?> propertiesPool, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }
}
