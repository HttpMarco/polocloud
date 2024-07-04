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

package dev.httpmarco.polocloud.addon.sign.configuration;

import dev.httpmarco.polocloud.addon.sign.CloudSignState;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class LayoutConfiguration {

    private final List<LayoutCategory> categories = new ArrayList<>();

    public LayoutConfiguration() {
        var defaultLayout = new LayoutCategory("default");
        defaultLayout.layouts().put(CloudSignState.SEARCHING, List.of(new LayoutTick(new String[]{" ", "Searching for", "server...", " "}, -1)));
        this.categories.add(defaultLayout);
    }
}
