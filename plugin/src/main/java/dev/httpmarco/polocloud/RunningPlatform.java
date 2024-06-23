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

package dev.httpmarco.polocloud;

import dev.httpmarco.polocloud.api.common.CloudMemoryCalculator;
import dev.httpmarco.polocloud.api.packets.general.OperationDoublePacket;
import dev.httpmarco.polocloud.api.packets.service.CloudServiceStateChangePacket;
import dev.httpmarco.polocloud.api.services.ServiceState;
import dev.httpmarco.polocloud.runner.CloudInstance;

public class RunningPlatform {

    // todo trash but works
    public RunningPlatform() {
        CloudInstance.instance().client().transmitter().responder("service-memory", communicationProperty -> new OperationDoublePacket(CloudMemoryCalculator.usedMemory()));
    }

    public void changeToOnline() {
        CloudInstance.instance().client().transmitter().sendPacket(new CloudServiceStateChangePacket(CloudInstance.SELF_ID, ServiceState.ONLINE));
    }
}
