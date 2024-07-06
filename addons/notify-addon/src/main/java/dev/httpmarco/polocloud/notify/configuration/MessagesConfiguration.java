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

package dev.httpmarco.polocloud.notify.configuration;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class MessagesConfiguration {

    private final String serviceStarting;
    private final String serviceStarted;
    private final String serviceStopped;

    public MessagesConfiguration() {
        this.serviceStarting = "<gray>The service <aqua>%service%<gray> is now starting.";
        this.serviceStarted = "<gray>The service <aqua>%service%<gray> has been started.";
        this.serviceStopped = "<gray>The service <aqua>%service%<gray> was stopped.";
    }
}
