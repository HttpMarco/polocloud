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

package dev.httpmarco.polocloud.runner.groups;

import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

//todo fix duplicated code
@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class InstanceGroup implements CloudGroup {

    private final String name;
    private final PlatformVersion platform;
    private final int memory;
    private final int minOnlineServices;


    @Override
    public PropertiesPool<GroupProperties<?>> properties() {
        //todo
        return null;
    }

    @Override
    public int onlineAmount() {
        //todo
        return 0;
    }

    @Override
    public void update() {
        //todo
    }
}
