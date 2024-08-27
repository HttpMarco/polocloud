package dev.httpmarco.polocloud.node.terminal.setup.impl;

import dev.httpmarco.polocloud.api.platforms.PlatformType;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.platforms.Platform;
import dev.httpmarco.polocloud.node.terminal.setup.Setup;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public final class PlatformSetup extends Setup {

    public PlatformSetup() {
        super("Platform-Setup");

        question("id", "What is the name of the new platform?", it -> Node.instance().platformService().find(it.first()) == null);
        question("type", "Which type have the new platform?", stringMapPair -> Arrays.stream(PlatformType.values()).map(Enum::name).toList(), it -> Arrays.stream(PlatformType.values()).anyMatch(s -> s.name().equalsIgnoreCase(it.first())));

        question("first-version", "Do you want to add the fist version", it -> List.of("yes", "no"), it -> {

            var result = it.first();

            if (result.equalsIgnoreCase("yes")) {
                // add new version questions
                question("first-version-number", "What is the id of the new version?", s -> {
                    Path.of("local/platforms/" + it.second().get("id") + "/" + s.first() + "/").toFile().mkdirs();
                    return true;
                });
                question("first-version-type", "Have the version a url or a static path?", s -> List.of("url", "file"), s -> {
                    var resultType = s.first();

                    if (resultType.equalsIgnoreCase("url")) {
                        question("first-version-number", "What the url of the new version jar?", url -> url.first().startsWith("http"));
                        return true;
                    }

                    if (resultType.equalsIgnoreCase("file")) {
                        question("first-version-file", "What is the name of the jar file? Current location dir &8'&flocal/platforms/" + it.second().get("id") + "/" + it.second().get("first-version-number") + "&8'&7", pathContext -> pathContext.first().endsWith(".jar") && Files.exists(Path.of("local/platforms/" + it.second().get("id") + "/" + pathContext.second().get("first-version-number") + "/" + pathContext.first())));
                        return true;
                    }
                    return false;
                });
            }
            return result.equalsIgnoreCase("yes") || result.equalsIgnoreCase("false");
        });
    }

    @Override
    public void complete(@NotNull Map<String, String> context) {
        var id = context.get("id");
        var type = PlatformType.valueOf(context.get("type"));

        var platform = new Platform(id, type, List.of(), List.of(), List.of());
        var platformService = Node.instance().platformService();

        if (Boolean.parseBoolean(context.get("first-version"))) {
            // the user want the first version of this platform
            var versionType = context.get("first-version-type");

        }

        platformService.platforms().add(platform);
        platformService.update();

        log.info("Successfully create new platform &8'&f{}&8'", id);
    }
}
