package dev.httpmarco.polocloud.node.platforms.util;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import dev.httpmarco.polocloud.node.Node;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class PlatformGroupDisplayTypeAdapter implements JsonDeserializer<PlatformGroupDisplay>, JsonSerializer<PlatformGroupDisplay> {

    @Override
    public PlatformGroupDisplay deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var object = (JsonObject) json;
        var platform = Node.instance().platformService().platform(object.get("platform").getAsString());

        return new PlatformGroupDisplay(platform.platform(), object.get("version").getAsString(), platform.type());
    }

    @Override
    public JsonElement serialize(@NotNull PlatformGroupDisplay src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();

        object.addProperty("platform", src.platform());
        object.addProperty("version", src.version());

        return object;
    }
}
