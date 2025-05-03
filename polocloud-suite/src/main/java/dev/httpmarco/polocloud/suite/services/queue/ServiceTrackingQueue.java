package dev.httpmarco.polocloud.suite.services.queue;

import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import dev.httpmarco.polocloud.suite.services.ClusterLocalServiceImpl;
import dev.httpmarco.polocloud.suite.services.ClusterServiceProviderImpl;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.net.Socket;

@Log4j2
public final class ServiceTrackingQueue extends Thread {

    private final ClusterServiceProviderImpl serviceProvider;

    public ServiceTrackingQueue(ClusterServiceProviderImpl serviceProvider) {
        super("ServiceTrackingQueue");

        this.serviceProvider = serviceProvider;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!isInterrupted()) {
            for (ClusterService service : serviceProvider.findAll()) {
                if (service instanceof ClusterLocalServiceImpl localService) {
                    if(service.state() == ClusterServiceState.STARTING) {
                        if(pingService(localService.port())) {
                            localService.changeState(ClusterServiceState.ONLINE);
                            log.info("Service &8'&f{}&8' &7is now online&8!", localService.name());
                        }
                    }
                }
            }
            try {
                sleep(1000);
            } catch (InterruptedException ignore) {
            }
        }
    }

    private boolean pingService(int port) {
        try (var socket = new Socket("localhost", port)) {
            return socket.isConnected();
        } catch (Exception e) {
            return false;
        }
    }
}
