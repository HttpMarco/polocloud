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

package dev.httpmarco.polocloud.api;

import dev.httpmarco.polocloud.api.events.EventNode;
import dev.httpmarco.polocloud.api.groups.CloudGroupProvider;
import dev.httpmarco.polocloud.api.player.CloudPlayerProvider;
import dev.httpmarco.polocloud.api.properties.PropertyPool;
import dev.httpmarco.polocloud.api.services.CloudServiceProvider;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public abstract class CloudAPI {

    @Getter
    private static CloudAPI instance;

    @SneakyThrows
    public CloudAPI() {
        instance = this;
    }

    public abstract CloudGroupProvider groupProvider();

    public abstract CloudServiceProvider serviceProvider();

    public abstract CloudPlayerProvider playerProvider();

    public abstract PropertyPool globalProperties();

    public abstract EventNode globalEventNode();

}
