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

import java.io.Serializable;
import java.util.HashMap;

@Getter
@Accessors(fluent = true)
public final class PropertyPool implements Serializable {

    // single properties of current pool
    private final HashMap<String, Object> properties = new HashMap<>();

    public boolean has(@NotNull Property<?> key) {
        return properties.containsKey(key.id());
    }

    public void appendAll(@NotNull PropertyPool propertyPool) {
        this.properties.putAll(propertyPool.properties);
    }

    public <R, P extends Property<R>> void put(@NotNull P property, R value) {
        this.put(property.id(), value);
    }

    public void put(@NotNull String propertyId, Object value) {
        this.properties.put(propertyId, value);
    }

    public void remove(@NotNull Property<?> property) {
        this.remove(property.id());
    }

    public void remove(@NotNull String propertyId) {
        this.properties.remove(propertyId);
    }

    @SuppressWarnings("unchecked")
    public <P> P property(@NotNull Property<P> property) {
        return (P) this.properties.get(property.id());
    }
}
