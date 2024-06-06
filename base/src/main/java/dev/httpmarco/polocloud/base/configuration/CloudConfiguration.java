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

package dev.httpmarco.polocloud.base.configuration;

import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.properties.CloudProperty;
import dev.httpmarco.polocloud.base.node.ExternalNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class CloudConfiguration {

    private final UUID clusterId;
    private final String clusterName;
    private final int maxMemory;

    private final ExternalNode[] externalNodes;
    private final PropertiesPool<CloudProperty<?>> properties = new PropertiesPool<>();

    public CloudConfiguration() {
        this.clusterId = UUID.randomUUID();
        this.clusterName = "node-1";
        this.maxMemory = 10000;
        this.externalNodes = new ExternalNode[0];
    }
}
