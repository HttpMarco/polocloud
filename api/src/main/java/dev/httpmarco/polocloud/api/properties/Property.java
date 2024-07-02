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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@Accessors(fluent = true)
@Getter
@AllArgsConstructor
public class Property<T> {

    private String id;
    private Type type;

    @Contract("_ -> new")
    public static @NotNull Property<String> String(String id) {
        // todo save
        return new Property<>(id, Type.STRING);
    }

    @Contract("_ -> new")
    public static @NotNull Property<Integer> Integer(String id) {
        // todo save
        return new Property<>(id, Type.INTEGER);
    }

    @Contract("_ -> new")
    public static @NotNull Property<Boolean> Boolean(String id) {
        // todo save
        return new Property<>(id, Type.BOOLEAN);
    }

    @Getter
    @Accessors(fluent = true)
    public enum Type {
        STRING(s -> s),
        INTEGER(Integer::parseInt),
        BOOLEAN(Boolean::parseBoolean);

        private final Function<String, Object> parser;

        Type(Function<String, Object> parser) {
            this.parser = parser;
        }

    }
}
