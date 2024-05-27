package dev.httpmarco.polocloud.base.groups.platforms;

import dev.httpmarco.polocloud.base.groups.CloudGroupPlatformService;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import dev.httpmarco.polocloud.runner.RunnerBootstrap;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.util.Objects;

public final class VelocityPlatform extends PaperMCPlatform {

    public VelocityPlatform() {
        super("velocity", true);
    }

    @Override
    @SneakyThrows
    public void prepare(LocalCloudService localCloudService) {
        var propertiesPath = localCloudService.runningFolder().resolve("velocity.toml");

        Files.writeString(localCloudService.runningFolder().resolve("forwarding.secret"), CloudGroupPlatformService.PROXY_SECRET);

        if (Files.exists(propertiesPath)) {
            //todo
        } else {
            Files.copy(Objects.requireNonNull(RunnerBootstrap.LOADER.getResourceAsStream("server-files/velocity/velocity.toml")), localCloudService.runningFolder().resolve("velocity.toml"));
        }
    }
}
