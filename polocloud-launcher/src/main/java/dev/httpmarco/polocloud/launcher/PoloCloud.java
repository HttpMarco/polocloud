/**
 * Copyright 2024 Polocloud / https://github.com/HttpMarco/polocloud
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.httpmarco.polocloud.launcher;

import dev.httpmarco.polocloud.launcher.utils.ManifestReader;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.channels.FileLock;

@Getter
@Accessors(fluent = true)
public final class PoloCloud {

    @Getter
    private static PoloCloud launcher;
    @Getter
    private static String version;

    // allow to check if the cloud is running -> Deny multiple instances
    private FileLock polocloudLock;

    public PoloCloud() {
        launcher = this;
        version = ManifestReader.detectVersion();

        // allow us to add more dependencies to the node at runtime
        // todo: implement this lock
        // todo: check version and maybe update this

        var process = new PoloCloudProcess();
        process.start();
    }
}