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

package dev.httpmarco.polocloud.base.common;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.properties.PropertyPool;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public final class PropertiesPoolSerializer implements JsonSerializer<PropertyPool>, JsonDeserializer<PropertyPool> {

    @Override
    public @NotNull PropertyPool deserialize(@NotNull JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var propertiesPool = new PropertyPool();
        jsonElement.getAsJsonObject().asMap().forEach((s, property) -> propertiesPool.pool().put(s, property.getAsJsonPrimitive().getAsString()));
        return propertiesPool;
    }

    @Override
    public @NotNull JsonElement serialize(@NotNull PropertyPool propertyPool, Type type, JsonSerializationContext jsonSerializationContext) {
        var object = new JsonObject();
        propertyPool.pool().forEach((id, o) -> object.add(id, new JsonPrimitive(o)));
        return object;
    }
}
