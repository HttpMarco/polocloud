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

package dev.httpmarco.polocloud.hubcommand.configuration;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class MessagesConfiguration {

    private final String prefix;
    private final String alreadyOnFallback;
    private final String noFallbackFound;
    private final String successfullyConnected;

    public MessagesConfiguration() {
        this.prefix = "<gradient:#00fdee:#118bd1><bold>PoloCloud </bold><dark_gray>| <reset>";
        this.alreadyOnFallback = "<red>You are already connected to a fallback service<gray>!";
        this.noFallbackFound = "<red>No fallback server was found<gray>!";
        this.successfullyConnected = "<gray>You have been connected to <aqua>%server% <gray>successfully.";
    }
}
