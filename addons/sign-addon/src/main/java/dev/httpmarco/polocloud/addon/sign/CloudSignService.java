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

package dev.httpmarco.polocloud.addon.sign;

import dev.httpmarco.polocloud.addon.sign.configuration.CloudSignLayoutService;
import dev.httpmarco.polocloud.api.CloudAPI;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
public class CloudSignService {

    @Getter
    private static CloudSignService instance;

    private final List<CloudSign> signs = new ArrayList<>();

    private final CloudSignFactory serviceSignFactory;
    private final CloudSignAnimationRunner signAnimationRunner;
    private final CloudSignLayoutService signLayoutService = new CloudSignLayoutService();

    public CloudSignService(CloudSignFactory factory) {
        instance = this;
        this.serviceSignFactory = factory;
        this.signAnimationRunner = new CloudSignAnimationRunner();
    }

    public void registerSign(String group, String world, int x, int y, int z) {
        this.signs.add(new CloudSign(group, world, x, y, z));
        this.serviceSignFactory.prependSearchingSign(group);
    }

    public boolean existsSign(String world, int x, int y, int z) {
        return this.signs.stream().anyMatch(it -> it.world().equals(world) && it.x() == x && it.y() == y && it.z() == z);
    }

    public CloudSign sign(String world, int x, int y, int z) {
        return this.signs.stream().filter(it -> it.world().equals(world) && it.x() == x && it.y() == y && it.z() == z).findFirst().orElse(null);
    }

    public void tick() {
        this.signAnimationRunner.tick();
    }

    public void connectPlayer(String world, int x, int y, int z, UUID uuid) {
        System.out.println("12");
        if (!existsSign(world, x, y, z)) {
            return;
        }        System.out.println("13");
        var sign = sign(world, x, y, z);

        if (sign.cloudService() == null) {
            return;
        }
        System.out.println("14");
        CloudAPI.instance().playerProvider().find(uuid).connectToServer(sign.cloudService());
    }
}
