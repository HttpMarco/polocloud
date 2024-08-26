package dev.httpmarco.polocloud.node.terminal.setup.impl;

import dev.httpmarco.polocloud.api.platforms.PlatformType;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.platforms.Platform;
import dev.httpmarco.polocloud.node.terminal.setup.Setup;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public final class PlatformSetup extends Setup {

    public PlatformSetup() {
        super("Platform-Setup");

        question("id", "What is the name of the new platform?", it -> Node.instance().platformService().find(it.first()) == null);
        question("type", "Which type have the new platform?", stringMapPair -> Arrays.stream(PlatformType.values()).map(Enum::name).toList(), it -> Arrays.stream(PlatformType.values()).anyMatch(s -> s.name().equalsIgnoreCase(it.first())));
    }

    @Override
    public void complete(@NotNull Map<String, String> context) {
        var id = context.get("id");
        var type = PlatformType.valueOf(context.get("type"));

        var platform = new Platform(id, type, List.of(), List.of(), List.of());

        var platformService = Node.instance().platformService();
        platformService.platforms().add(platform);
        platformService.update();

        log.info("Successfully create new platform &8'&f{}&8'", id);
    }
}
