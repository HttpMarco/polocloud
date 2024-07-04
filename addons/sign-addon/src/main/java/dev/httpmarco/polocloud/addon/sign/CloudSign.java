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

import dev.httpmarco.polocloud.addon.sign.configuration.LayoutTick;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
public class CloudSign {

    private final String group;
    private final String layoutId;

    private final String world;
    private final int x;
    private final int y;
    private final int z;

    @Setter
    private long animationTick = 0;

    @Setter
    private CloudSignState state = CloudSignState.SEARCHING;

    public CloudSign(String group, String world, int x, int y, int z) {
        this.z = z;
        this.y = y;
        this.x = x;
        this.world = world;
        this.group = group;

        // todo custom
        this.layoutId = "default";

        this.update();
    }

    public void update() {
        CloudSignService.instance().serviceSignFactory().print(this);
    }

    public LayoutTick currentLayout() {
        long currentStep = 0;
        for (var tick : CloudSignService.instance().signLayoutService().find(this.layoutId()).layouts().get(state)) {
            currentStep += tick.holdTime();

            if (animationTick <= currentStep) {
                return tick;
            }
        }
        return null;
    }

    public void tick() {
        animationTick++;
        if (animationTick >= maxAnimationLength()) {
            animationTick = 0;
        }
        update();
    }

    private long maxAnimationLength() {
        return CloudSignService.instance().signLayoutService().find(this.layoutId()).layouts().get(state).stream().mapToLong(LayoutTick::holdTime).sum();
    }
}
