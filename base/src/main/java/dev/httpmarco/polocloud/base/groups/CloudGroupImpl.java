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

package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public class CloudGroupImpl extends CloudGroup implements Serializable {

    public CloudGroupImpl(String name, PlatformVersion platform, int memory, int minOnlineService) {
        super(name, platform, memory, minOnlineService);
    }

    @Override
    public CompletableFuture<Integer> onlineAmountAsync() {
        return CompletableFuture.completedFuture(CloudAPI.instance().serviceProvider().services(this).size());
    }

    @Override
    public String toString() {
        return "platform='" + platform().version() + '\'' +
                ", memory=" + memory() +
                ", minOnlineServices=" + minOnlineService();
    }

    @Override
    public boolean equals(Object obj) {
        return this.name().equals(((CloudGroup)obj).name());
    }
}
