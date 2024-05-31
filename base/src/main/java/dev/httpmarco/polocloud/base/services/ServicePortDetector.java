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

package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public final class ServicePortDetector {

    public static int detectServicePort(CloudGroup group) {
        var servicePort = group.properties().has(GroupProperties.PORT_RANGE) ? group.properties().property(GroupProperties.PORT_RANGE) : 25565;

        while (isUsed(servicePort)) {
            servicePort++;
        }

        return servicePort;
    }

    private static boolean isUsed(int port) {
        for (var service : CloudAPI.instance().serviceProvider().services()) {
            if (service.port() == port) {
                return true;
            }
        }
        try (var testSocket = new ServerSocket()) {
            testSocket.bind(new InetSocketAddress(port));
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
