package dev.httpmarco.polocloud.suite.utils;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.platform.PlatformType;
import dev.httpmarco.polocloud.suite.services.ClusterLocalService;
import dev.httpmarco.polocloud.suite.services.ClusterLocalServiceImpl;
import lombok.experimental.UtilityClass;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

@UtilityClass
public class PortDetector {

    public int nextPort(ClusterGroup group) {
        var port = group.platform().type() == PlatformType.PROXY ? 25565 : 30000;
        while (isPortUsed(port)) {
            port = port + 2;
        }
        return port;
    }

    private boolean isPortUsed(int port) {
        for (final var service : Polocloud.instance().serviceProvider().findAll()) {
            if (service instanceof ClusterLocalService localService) {
                if (localService.port() == port) return true;
            }
        }
        try (final var serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(port));
            return false;
        } catch (Exception exception) {
            return true;
        }
    }
}
