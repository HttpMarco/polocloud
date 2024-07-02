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

package dev.httpmarco.polocloud.api.properties;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
@Accessors(fluent = true)
public class PropertyPool {

    private final Map<String, String> pool = new HashMap<>();

    public <V> void put(@NotNull Property<V> property, @NotNull V value) {
        this.pool.put(property.id(), value.toString());
    }

    public int size() {
        return this.pool.size();
    }

    public boolean has(@NotNull Property<?> property) {
        return pool.containsKey(property.id());
    }

    @SuppressWarnings("unchecked")
    public <T> T property(@NotNull Property<T> property) {
        return (T) property.type().parser().apply(pool.get(property.id()));
    }

}
