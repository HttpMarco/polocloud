package dev.httpmarco.polocloud.suite.configuration.serializer;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Locale;

public final class LocalSerializer implements JsonSerializer<Locale>, JsonDeserializer<Locale> {

    @Override
    public Locale deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Locale.of(json.getAsString());
    }

    @Override
    public JsonElement serialize(Locale src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getLanguage());
    }
}
