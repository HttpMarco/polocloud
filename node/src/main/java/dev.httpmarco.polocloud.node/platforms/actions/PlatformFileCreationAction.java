package dev.httpmarco.polocloud.node.platforms.actions;


import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Files;

@Slf4j
@Getter
@Accessors(fluent = true)
public final class PlatformFileCreationAction extends AbstractPlatformAction {

    private final String fileName;
    private final String content;

    public PlatformFileCreationAction(String fileName, String content) {
        super("file-creation");
        this.fileName = fileName;
        this.content = content;
    }

    @Override
    @SneakyThrows
    public void run(@NotNull ClusterLocalServiceImpl service) {
        log.debug("Write new file {} for service {} with content&8: {}", fileName, service.name(), content);
        Files.writeString(service.runningDir().resolve(fileName), content.replaceAll("%forwarding_secret%", Node.instance().serviceProvider().serviceProxyToken()));
    }
}
