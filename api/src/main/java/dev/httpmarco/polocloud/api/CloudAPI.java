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

import dev.httpmarco.polocloud.api.dependencies.DependencyService;
import dev.httpmarco.polocloud.api.events.Event;
import dev.httpmarco.polocloud.api.events.EventNode;
import dev.httpmarco.polocloud.api.groups.CloudGroupProvider;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.api.logging.Logger;
import dev.httpmarco.polocloud.api.logging.LoggerFactory;
import dev.httpmarco.polocloud.api.node.NodeService;
import dev.httpmarco.polocloud.api.properties.CloudProperty;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.services.CloudServiceProvider;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public abstract class CloudAPI {

    @Getter
    private static CloudAPI instance;

    private final LoggerFactory loggerFactory = new LoggerFactory();
    private final Logger logger = new Logger();

    private final DependencyService dependencyService = new DependencyService();

    @SneakyThrows
    public CloudAPI() {
        instance = this;

        //todo better loading of custom properties
        Class.forName(GroupProperties.class.getName());
        Class.forName(CloudProperty.class.getName());
    }

    public abstract NodeService nodeService();

    public abstract CloudGroupProvider groupProvider();

    public abstract CloudServiceProvider serviceProvider();

    public abstract PropertiesPool<CloudProperty<?>> globalProperties();

    public abstract EventNode globalEventNode();

}
