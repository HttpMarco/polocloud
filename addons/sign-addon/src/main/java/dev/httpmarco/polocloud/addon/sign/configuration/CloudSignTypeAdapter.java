/*
 * Copyright 2024 Mirco Lindenau | HttpMarco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.httpmarco.polocloud.addon.sign.configuration;

import com.google.gson.*;
import dev.httpmarco.polocloud.addon.sign.CloudSign;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public final class CloudSignTypeAdapter implements JsonDeserializer<CloudSign>, JsonSerializer<CloudSign> {

    @Override
    public CloudSign deserialize(@NotNull JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var object = jsonElement.getAsJsonObject();
        var group = object.get("group").getAsString();
        var world = object.get("world").getAsString();
        var x = object.get("x").getAsInt();
        var y = object.get("y").getAsInt();
        var z = object.get("z").getAsInt();

        return new CloudSign(group, world, x, y, z);
    }

    @Override
    public JsonElement serialize(@NotNull CloudSign sign, Type type, JsonSerializationContext jsonSerializationContext) {
        var object = new JsonObject();

        object.addProperty("group", sign.group());
        object.addProperty("world", sign.world());
        object.addProperty("x", sign.x());
        object.addProperty("y", sign.y());
        object.addProperty("z", sign.z());

        return object;
    }
}
