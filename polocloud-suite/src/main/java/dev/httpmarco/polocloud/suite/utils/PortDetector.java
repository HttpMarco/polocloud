package dev.httpmarco.polocloud.suite.utils;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.platform.PlatformType;
import dev.httpmarco.polocloud.suite.services.ClusterLocalService;
import dev.httpmarco.polocloud.suite.services.ClusterLocalServiceImpl;
import lombok.experimental.UtilityClass;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

@UtilityClass
public class PortDetector {

    public int nextPort(ClusterLocalServiceImpl service) {
        var port = service.group().platform().type() == PlatformType.PROXY ? 25565 : 30000;
        while (isPortUsed(port)) {
            port++;
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
