package dev.httpmarco.polocloud.node.i18n.serializer;

import com.google.gson.*;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Locale;

@Log4j2
public final class LocalSerializer implements JsonSerializer<Locale>, JsonDeserializer<Locale> {

    @Override
    public Locale deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Arrays.stream(Locale.getAvailableLocales()).filter(it -> it.getLanguage().equals(json.getAsString())).findFirst().orElseGet(() -> {
            log.warn("Could not find locale for language: {} Use default language 'en'", json.getAsString());
            return Locale.ENGLISH;
        });
    }

    @Contract("_, _, _ -> new")
    @Override
    public @NotNull JsonElement serialize(@NotNull Locale src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getLanguage());
    }
}
