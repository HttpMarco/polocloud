package dev.httpmarco.polocloud.base.common;

import com.google.gson.*;
import dev.httpmarco.osgan.files.json.JsonTypeAdapter;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;

import java.lang.reflect.Type;

public final class PropertiesPoolSerializer extends JsonTypeAdapter<PropertiesPool<?>> implements JsonSerializer<PropertiesPool<?>>, JsonDeserializer<PropertiesPool<?>> {

    @SuppressWarnings("unchecked")
    public PropertiesPoolSerializer() {
        super((Class<PropertiesPool<?>>) new PropertiesPool<>().getClass());
    }

    @Override
    public PropertiesPool<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var propertiesPool = new PropertiesPool<>();

        jsonElement.getAsJsonObject().asMap()
                .forEach((s, property) -> PropertiesPool.PROPERTY_LIST
                        .stream()
                        .filter(it -> it.id().equals(s))
                        .findFirst()
                        .ifPresentOrElse(prop -> propertiesPool.properties().put(prop, jsonDeserializationContext.deserialize(property, prop.type())), () -> {
                            CloudAPI.instance().logger().error("Unknown property found: " + s, null);
                        }));

        return propertiesPool;
    }

    @Override
    public JsonElement serialize(PropertiesPool<?> propertiesPool, Type type, JsonSerializationContext jsonSerializationContext) {
        var object = new JsonObject();
        propertiesPool.properties().forEach((property, o) -> object.add(property.id(), jsonSerializationContext.serialize(o)));
        return object;
    }
}
