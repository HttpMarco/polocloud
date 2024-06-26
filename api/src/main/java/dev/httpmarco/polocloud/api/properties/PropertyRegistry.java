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

import java.util.HashMap;
import java.util.Map;

public final class PropertyRegistry {

    private static final Map<String, PropertySupportTypes> registeredTypes = new HashMap<>();

    public static void register(String id, PropertySupportTypes type) {
        registeredTypes.put(id, type);
    }

    public static PropertySupportTypes findType(String id) {
        return registeredTypes.get(id);
    }
}