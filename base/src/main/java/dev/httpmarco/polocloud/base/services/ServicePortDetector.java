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
