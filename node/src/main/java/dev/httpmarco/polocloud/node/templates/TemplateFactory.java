package dev.httpmarco.polocloud.node.templates;

import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;
import dev.httpmarco.polocloud.node.util.DirectoryActions;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

@Slf4j
@UtilityClass
public final class TemplateFactory {

    public void cloneTemplate(@NotNull ClusterLocalServiceImpl localService) {

        for (var template : localService.group().templates()) {
            var templatePath = Path.of("templates/" + template);

            log.debug("Copy template {} to {}", template, localService.name());
            DirectoryActions.copyDirectoryContents(templatePath, localService.runningDir());
        }
    }
}
