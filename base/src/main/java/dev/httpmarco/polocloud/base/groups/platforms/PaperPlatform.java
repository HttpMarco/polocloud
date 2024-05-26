package dev.httpmarco.polocloud.base.groups.platforms;

import dev.httpmarco.polocloud.base.services.LocalCloudService;
import dev.httpmarco.polocloud.runner.RunnerBootstrap;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public final class PaperPlatform extends PaperMCPlatform{

    public PaperPlatform() {
        super("paper");
    }

    @Override
    @SneakyThrows
    public void prepare(LocalCloudService localCloudService) {
        // accept eula without cringe logs
        Files.writeString(localCloudService.runningFolder().resolve("eula.txt"), "eula=true");

        var serverProperties = localCloudService.runningFolder().resolve("server.properties").toFile();

        if (!Files.exists(serverProperties.toPath())) {
            // copy file from storage
            Files.copy(Objects.requireNonNull(RunnerBootstrap.LOADER.getResourceAsStream("server-files/spigot/server.properties")), localCloudService.runningFolder().resolve("server.properties"));
        }

        var properties = new Properties();

        try (var fileReader = new FileReader(serverProperties)) {
            properties.load(fileReader);
        }

        properties.setProperty("server-name", localCloudService.name());
        properties.setProperty("server-port", String.valueOf(localCloudService.port()));

        try (var fileWriter = new FileWriter(serverProperties)) {
            properties.store(fileWriter, null);
        }

        // manipulate velocity secret if
        var globalPaperProperty = localCloudService.runningFolder().resolve("config/paper-global.yml");

        if (Files.exists(globalPaperProperty)) {
            Yaml yaml = new Yaml();

            var paperProperties = (Map<String, Object>) yaml.load(globalPaperProperty.toString());
            var proxyProperties = (Map<String, Object>) paperProperties.get("proxies");
            var velocityProperties = (Map<String, Object>) proxyProperties.get("velocity");


            velocityProperties.put("enabled", true);
            velocityProperties.put("secret", "abc");

            var writer = new FileWriter(globalPaperProperty.toString());
            yaml.dump(paperProperties, writer);
        }
    }
}
