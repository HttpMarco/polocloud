package dev.httpmarco.polocloud.suite.services.queue;

import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.suite.services.ClusterLocalServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

@RequiredArgsConstructor
public class ServiceLogQueue extends Thread {

    private static final long LOG_UPDATE_CYCLE = 100;

    private final ClusterServiceProvider clusterServiceProvider;

    @Override
    @SneakyThrows
    public void run() {
        while (!isInterrupted()) {
            for (var service : clusterServiceProvider.findAll()) {
                if (service instanceof ClusterLocalServiceImpl localService) {
                    appendServiceLog(localService);
                }
            }
            try {
                Thread.sleep(LOG_UPDATE_CYCLE);
            } catch (InterruptedException ignore) {
            }
        }
    }

    @SneakyThrows
    private void appendServiceLog(@NotNull ClusterLocalServiceImpl service) {
        if (service.process() != null) {
            var inputStream = service.process().getInputStream();
            var bytes = new byte[2048];
            int length;

            var logs = new ArrayList<String>();

            while (inputStream.available() > 0 && (length = inputStream.read(bytes, 0, bytes.length)) != -1) {
                logs.addAll(Arrays.asList(new String(bytes, 0, length, StandardCharsets.UTF_8).split("\n")));
            }

            service.logs().addAll(logs);
            //todo
            //Node.instance().screenProvider().publish(service, logs);

            if (logs.isEmpty()) return;
            // Node.instance().eventProvider().factory().call(new ServiceLogEvent(service, logs));
        }
    }
}