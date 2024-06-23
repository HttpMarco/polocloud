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


public final class VelocityPlatform extends PaperMCPlatform {

    public VelocityPlatform() {

    }

    /*
    @Override
    @SneakyThrows
    public void prepare(LocalCloudService localCloudService) {
        var propertiesPath = localCloudService.runningFolder().resolve("velocity.toml");

        Files.writeString(localCloudService.runningFolder().resolve("forwarding.secret"), CloudGroupPlatformService.PROXY_SECRET);

        if (!Files.exists(propertiesPath)) {
            Files.copy(Objects.requireNonNull(RunnerBootstrap.LOADER.getResourceAsStream("server-files/velocity/velocity.toml")), propertiesPath);
        }

        // we must change the port and hostname
        FileConfig config = FileConfig.builder(propertiesPath).build();
        config.load();

        config.set("bind", localCloudService.hostname() + ":" + localCloudService.port());
        config.save();
        config.close();
    }

     */
}
