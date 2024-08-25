package dev.httpmarco.polocloud.node.services;

import dev.httpmarco.polocloud.node.Node;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class ClusterLocalServiceReadingThread extends Thread {

    private static final long LOG_UPDATE_CYCLE = 1000;

    @Override
    @SneakyThrows
    public void run() {
        while (!isInterrupted()) {
            for (var service : Node.instance().serviceProvider().services()) {
                if (service instanceof ClusterLocalServiceImpl localService) {
                    appendServiceLog(localService);
                }
            }
            Thread.sleep(LOG_UPDATE_CYCLE);
        }
    }

    @SneakyThrows
    private void appendServiceLog(@NotNull ClusterLocalServiceImpl service) {
        if (service.process() != null) {
            var inputStream = service.process().getInputStream();
            var bytes = new byte[2048];
            int length;
            while (inputStream.available() > 0 && (length = inputStream.read(bytes, 0, bytes.length)) != -1) {
                service.logs().addAll(Arrays.asList(new String(bytes, 0, length, StandardCharsets.UTF_8).split("\n")));
            }
        }
    }
}
