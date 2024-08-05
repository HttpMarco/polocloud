package dev.httpmarco.polocloud.node.platforms.actions;


import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;

@Slf4j
@Accessors(fluent = true)
@RequiredArgsConstructor
public final class PlatformFileCreationAction implements PlatformAction {

    @Getter
    private final String propertyId = "file-creation";
    private final String fileName;
    private final String content;

    @Override
    @SneakyThrows
    public void run(@NotNull ClusterLocalServiceImpl service) {
        log.debug("Write new file {} for service {} with content&8: {}", fileName, service.name(), content);
        Files.writeString(service.runningDir().resolve(fileName), content);
    }
}
