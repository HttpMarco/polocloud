package dev.httpmarco.polocloud.suite.configuration;

import dev.httpmarco.polocloud.suite.utils.GsonInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public final class SuiteConfig {

    private static final Path CONFIG_PATH = Paths.get("config.json");
    private static final Logger log = LogManager.getLogger(SuiteConfig.class);

    private final Locale language;
    private final ClusterConfig cluster;

    public SuiteConfig() {
        this.language = Locale.ENGLISH;
        this.cluster = new ClusterConfig();
    }

    public Locale language() {
        return language;
    }

    public ClusterConfig cluster() {
        return cluster;
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
