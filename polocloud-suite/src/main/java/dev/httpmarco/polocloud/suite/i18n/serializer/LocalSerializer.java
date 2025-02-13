package dev.httpmarco.polocloud.suite.i18n.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Locale;

public final class LocalSerializer implements JsonSerializer<Locale>, JsonDeserializer<Locale> {

    private static final Logger log = LoggerFactory.getLogger(LocalSerializer.class);

    @Override
    public Locale deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Arrays.stream(Locale.getAvailableLocales()).filter(it -> it.getLanguage().equals(json.getAsString())).findFirst().orElseGet(() -> {
            log.warn("Could not find locale for language: {} Use default language 'en'", json.getAsString());
            return Locale.ENGLISH;
        });
    }

    @Override
    public JsonElement serialize(Locale src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getLanguage());
    }
}