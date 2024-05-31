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

import dev.httpmarco.osgan.files.json.JsonUtils;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;

import java.util.List;

public interface CloudGroupProvider {

    boolean createGroup(String name, String platform, int memory, int minOnlineCount);

    boolean deleteGroup(String name);

    boolean isGroup(String name);

    CloudGroup group(String name);

    List<CloudGroup> groups();

    void update(CloudGroup cloudGroup);

    default CloudGroup fromPacket(CodecBuffer buffer) {
        CloudGroup group = JsonUtils.fromJson(buffer.readString(), CloudGroup.class);
        if (this.isGroup(group.name())) return group;
        return null;
    }

}