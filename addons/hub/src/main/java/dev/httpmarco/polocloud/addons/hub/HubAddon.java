package dev.httpmarco.polocloud.addons.hub;

import dev.httpmarco.polocloud.api.utils.JsonPoint;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Accessors(fluent = true)
public final class HubAddon {

    private static final Path CONFIGURATION_PATH = Path.of("plugins/polocloud-hub/config.json");

    @Getter
    private static HubAddon instance;

    private final HubConfig config;

    public HubAddon(boolean minimessage) {
        instance = this;

        this.config = loadConfiguration(minimessage);
    }

    @SneakyThrows
    public HubConfig loadConfiguration(boolean minimessage) {
        if (!Files.exists(CONFIGURATION_PATH)) {
            CONFIGURATION_PATH.getParent().toFile().mkdirs();

            var errorOccurred = minimessage ? "<red>Ein Fehler ist aufgetreten" : "§cEin Fehler ist aufgetreten";
            var noPlayer = minimessage ? "<red>Für diese Command musst du ein Spieler sein" : "§cFür diese Command musst du ein Spieler sein";
            var alreadyConnected = minimessage ? "<red>Du bist bereits auf diesem Server" : "§cDu bist bereits auf diesem Server";
            var noFallbackFound = minimessage ? "Es konnte <red>kein <gray>Fallback-Server gefunden werden" : "Es konnte §ckein §7Fallback-Server gefunden werden";
            var successfullyConnected = minimessage ? "Du bist nun auf dem Server <aqua>%s" : "Du bist nun auf dem Server §b%s";

            var config = new HubConfig(new String[]{"hub", "lobby", "leave", "l"}, new HubMessages(errorOccurred, noPlayer, alreadyConnected, noFallbackFound, successfullyConnected));

            Files.writeString(CONFIGURATION_PATH, JsonPoint.PRETTY_JSON.toJson(config));
            return config;
        }
        return JsonPoint.PRETTY_JSON.fromJson(Files.readString(CONFIGURATION_PATH), HubConfig.class);
    }
}
