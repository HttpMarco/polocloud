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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Accessors(fluent = true)
public final class PropertiesPool<T extends Property<?>> implements Serializable {

    // all properties of cluster
    public static final List<Property<?>> PROPERTY_LIST = new ArrayList<>();
    // single properties of current pool
    private final HashMap<T, Object> properties = new HashMap<>();

    public boolean has(T key) {
        return properties.containsKey(key);
    }

    public void appendAll(PropertiesPool<?> propertiesPool) {
        this.properties.putAll((Map<? extends T, ?>) propertiesPool.properties);
    }

    @SuppressWarnings("unchecked")
    public <R, P extends Property<R>> void put(P property, R value) {
        this.properties.put((T) property, value);
    }

    public void putRaw(Property<?> property, Object value) {
        this.properties.put((T) property, value);
    }

    public void remove(T property) {
        this.properties.remove(property);
    }

    @SuppressWarnings("unchecked")
    public <P> P property(Property<P> property) {
        return (P) this.properties.get(property);
    }

    public static Property<?> property(String id) {
        return PROPERTY_LIST.stream().filter(it -> it.id().equals(id)).findFirst().orElse(null);
    }
}
