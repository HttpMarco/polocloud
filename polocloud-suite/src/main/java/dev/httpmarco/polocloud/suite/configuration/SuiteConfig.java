package dev.httpmarco.polocloud.suite.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.httpmarco.polocloud.suite.cluster.data.LocalSuiteData;
import dev.httpmarco.polocloud.suite.i18n.serializer.LocalSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public final class SuiteConfig {

    private static final Path CONFIG_PATH = Paths.get("config.json");
    private static final Gson CONFIG_GSON = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Locale.class, new LocalSerializer()).create();
    private static final Logger log = LogManager.getLogger(SuiteConfig.class);

    private final Locale language;
    private final LocalSuiteData localSuite;

    public SuiteConfig() {
        this.language = Locale.ENGLISH;
        this.localSuite = new LocalSuiteData("suite-1", "127.0.0.1", 8439);
    }

    public Locale language() {
        return language;
    }

    public LocalSuiteData localSuiteData() {
        return localSuite;
    }

    public static SuiteConfig load() {
        var defaultConfig = new SuiteConfig();
        if (Files.exists(CONFIG_PATH)) {
            try {
                return CONFIG_GSON.fromJson(Files.readString(CONFIG_PATH), SuiteConfig.class);
            } catch (IOException e) {
                log.warn("Failed to load suite config! Using default configuration.");
                return defaultConfig;
            }
        }
        try {
            Files.writeString(CONFIG_PATH, CONFIG_GSON.toJson(defaultConfig));
        } catch (IOException e) {
            log.warn("Failed to write suite config! Using default configuration.");
        }
        return defaultConfig;
    }
}
