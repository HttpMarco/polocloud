package dev.httpmarco.polocloud.node.platforms.serializer;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.platforms.exceptions.NotDetectablePlatformException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public final class SharedPlatformSerializer implements JsonSerializer<SharedPlatform>, JsonDeserializer<SharedPlatform> {

    @Contract(pure = true)
    @Override
    public @Nullable JsonElement serialize(SharedPlatform sharedPlatform, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Contract(pure = true)
    @Override
    public @NotNull SharedPlatform deserialize(@NotNull JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var data = jsonElement.getAsJsonObject();

        var platformName = data.get("name").getAsString();
        var platformVersion = (Version) jsonDeserializationContext.deserialize(data.get("version"), Version.class);

        var platform = Node.instance().platformProvider().searchPlatformVersion(platformName, platformVersion.originalVersion());

        if (platform == null) {
            throw new NotDetectablePlatformException(platformName,platformVersion.originalVersion());
        }

        return platform.share();
    }
}
