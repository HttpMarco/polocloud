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

package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.properties.Property;

public final class GroupProperties {

    public static Property<String> TEMPLATES = Property.String("templates");

    public static Property<Boolean> DEBUG_MODE = Property.Boolean("debugMode");

    public static Property<Boolean> MAINTENANCE = Property.Boolean("maintenance");

    public static Property<Integer> PORT_RANGE = Property.Integer("portRange");

    public static Property<Integer> MAX_SERVICES = Property.Integer("maxOnlineServices");

    public static Property<Integer> MAX_PLAYERS = Property.Integer("maxPlayers");

    public static Property<Boolean> FALLBACK = Property.Boolean("fallback");

    public static Property<Boolean> STATIC = Property.Boolean("static");

}