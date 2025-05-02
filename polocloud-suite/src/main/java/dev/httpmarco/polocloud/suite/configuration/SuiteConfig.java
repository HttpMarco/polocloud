package dev.httpmarco.polocloud.suite.configuration;

import dev.httpmarco.polocloud.suite.cluster.ClusterConfig;
import dev.httpmarco.polocloud.suite.cluster.configuration.ClusterLocalConfig;
import dev.httpmarco.polocloud.suite.utils.GsonInstance;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

@Getter
@Accessors(fluent = true)
public final class SuiteConfig {

    private static final Path CONFIG_PATH = Paths.get("config.json");
    private static final Logger log = LogManager.getLogger(SuiteConfig.class);

    private final Locale language;

    @Setter
    private ClusterConfig cluster;

    private final LocalConfig local;

    public SuiteConfig() {
        this.language = Locale.ENGLISH;
        this.local = LocalConfig.DEFAULT;
        this.cluster = new ClusterLocalConfig("suite-1", 8479);
    }

    public static SuiteConfig load() {
        var defaultConfig = new SuiteConfig();
        if (Files.exists(CONFIG_PATH)) {
            try {
                defaultConfig = GsonInstance.DEFAULT.fromJson(Files.readString(CONFIG_PATH), SuiteConfig.class);
                // we write the new data after reading it to update the file
            } catch (IOException e) {
                log.warn("Failed to load suite config! Using default configuration.");
                return defaultConfig;
            }
        }
        try {
            Files.writeString(CONFIG_PATH, GsonInstance.DEFAULT.toJson(defaultConfig));
        } catch (IOException e) {
            log.warn("Failed to write suite config! Using default configuration.");
        }
        return defaultConfig;
    }

    public void update() {
        try {
            Files.writeString(CONFIG_PATH, GsonInstance.DEFAULT.toJson(this));
        } catch (IOException e) {
            log.warn("Failed to write suite config! Using default configuration.");
        }
    }
}
