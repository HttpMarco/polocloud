package dev.httpmarco.polocloud.node.properties;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.properties.PropertyRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public final class PropertiesPoolSerializer implements JsonDeserializer<PropertiesPool>, JsonSerializer<PropertiesPool> {

    private static final Logger log = LogManager.getLogger(PropertiesPoolSerializer.class);

    @Contract("_, _, _ -> new")
    @Override
    public @NotNull PropertiesPool deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var object = (JsonObject) json;
        var propertiesPool = new PropertiesPool();

        for (var key : object.keySet()) {
            var property = PropertyRegister.byName(key);

            if(property == null) {
                log.warn("Property {} not found in register!", key);
                continue;
            }

            propertiesPool.pool().put(property, context.deserialize(object.get(key), property.clazz()));
        }
        return propertiesPool;
    }

    @Override
    public @NotNull JsonElement serialize(@NotNull PropertiesPool pool, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        pool.pool().forEach((property, o) -> object.add(property.name(), context.serialize(o)));
        return object;
    }
}
