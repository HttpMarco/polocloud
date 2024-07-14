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

import dev.httpmarco.polocloud.base.node.Cluster;
import dev.httpmarco.polocloud.api.properties.PropertyPool;
import dev.httpmarco.polocloud.base.node.LocalNode;
import dev.httpmarco.pololcoud.common.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.HashSet;

@Getter
@Accessors(fluent = true)
public final class CloudConfiguration {

    private final LocalNode localNode;
    private final int maxMemory;

    private final PropertyPool properties = new PropertyPool();

    @Setter
    private Cluster cluster;

    public CloudConfiguration() {
        this.localNode = new LocalNode("node-1", "127.0.0.1", 9090);
        this.cluster = new Cluster(null, "default-cluster", StringUtils.randomString(16), new HashSet<>());
        this.maxMemory = 10000;
    }
}
