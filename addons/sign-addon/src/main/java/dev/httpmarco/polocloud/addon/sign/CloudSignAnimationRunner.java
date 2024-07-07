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
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Accessors(fluent = true)
public final class CloudSignAnimationRunner {

    private final Map<CloudSignState, Long> stateTicks = new ConcurrentHashMap<>();
    // collect the max length of animations
    private final Map<CloudSignState, Long> maxStateTicks = new HashMap<>();
    // last ticked frame
    @Getter
    private final Map<CloudSignState, LayoutTick> currentTickedLayout = new HashMap<>();


    public CloudSignAnimationRunner() {
        for (var state : CloudSignState.values()) {
            var layouts = CloudSignService.instance().signLayoutService().getLayoutConfiguration().layouts();
            if (layouts.containsKey(state)) {
                this.stateTicks.put(state, 0L);

                // calculate the maximum ticks
                var ticks = layouts.get(state);
                this.maxStateTicks.put(state, ticks.stream().mapToLong(LayoutTick::holdingTicks).sum());

                this.currentTickedLayout.put(state, layouts.get(state).get(0));
            }
        }
    }

    public void tick() {
        for (var state : stateTicks.keySet()) {
            var tick = stateTicks.get(state);
            var maxTickRate = maxStateTicks.get(state);

            tick = tick >= maxTickRate ? 0L : tick + 1;

            this.stateTicks.put(state, tick);

            var frame = generateNewFrame(state, tick);

            if (frame != null) {
                this.currentTickedLayout.put(state, frame);
            }
        }

        CloudSignService.instance().signConfiguration().signs().forEach(CloudSign::update);
    }

    private LayoutTick generateNewFrame(CloudSignState state, long currentTick) {
        long tempTick = 0L;

        for (var frame : CloudSignService.instance().signLayoutService().getLayoutConfiguration().layouts().get(state)) {
            tempTick += frame.holdingTicks();

            if (currentTick <= tempTick) {
                return frame;
            }
        }

        return null;
    }
}
