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

package dev.httpmarco.polocloud.base.groups.platforms;

import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class Platform {

    private final boolean proxy;
    private final Set<PlatformVersion> possibleVersions = new HashSet<>();

    public abstract void download(String version);

    public abstract void prepare(LocalCloudService localCloudService);

    public String[] platformsEnvironment() {
        return new String[0];
    }

    public String[] platformsArguments() {
        return new String[0];
    }
}