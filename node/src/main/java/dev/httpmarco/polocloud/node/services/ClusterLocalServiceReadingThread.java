package dev.httpmarco.polocloud.node.services;

import dev.httpmarco.polocloud.api.event.impl.services.log.ServiceLogEvent;
import dev.httpmarco.polocloud.node.Node;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

@Log4j2
public final class ClusterLocalServiceReadingThread extends Thread {

    private static final long LOG_UPDATE_CYCLE = 100;

    @Override
    @SneakyThrows
    public void run() {
        while (!isInterrupted()) {
            for (var service : Node.instance().serviceProvider().services()) {
                if (service instanceof ClusterLocalServiceImpl localService) {
                    appendServiceLog(localService);
                }
            }
            try {
                Thread.sleep(LOG_UPDATE_CYCLE);
            }catch (InterruptedException ignore) {}
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
            Node.instance().screenProvider().publish(service, logs);

            if (logs.isEmpty()) return;
            Node.instance().eventProvider().factory().call(new ServiceLogEvent(service, logs));
        }
    }
}
