package dev.httpmarco.polocloud.base.common;

import com.google.gson.*;
import dev.httpmarco.osgan.files.json.JsonTypeAdapter;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;

import java.lang.reflect.Type;

public final class PropertiesPoolSerializer extends JsonTypeAdapter<PropertiesPool<?>> {

    public PropertiesPoolSerializer() {
        super((Class<PropertiesPool<?>>) new PropertiesPool<>().getClass());
    }

    @Override
    public PropertiesPool<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {


        return null;
    }

    @Override
    public JsonElement serialize(PropertiesPool<?> propertiesPool, Type type, JsonSerializationContext jsonSerializationContext) {
        var object = new JsonObject();
        propertiesPool.properties().forEach((property, o) -> object.add(property.id(), jsonSerializationContext.serialize(o)));
        return object;
    }
}
