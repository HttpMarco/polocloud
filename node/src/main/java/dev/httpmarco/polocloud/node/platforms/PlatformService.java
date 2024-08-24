package dev.httpmarco.polocloud.node.platforms;

import dev.httpmarco.polocloud.node.util.JsonUtils;
import dev.httpmarco.polocloud.node.util.StringUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

@Slf4j
@Getter
@Accessors(fluent = true)
public final class PlatformService {

    public static String FORWARDING_SECRET = StringUtils.randomString(8);
    private static final String VERSIONS_URL = "https://raw.githubusercontent.com/HttpMarco/polocloud/dev/release/versions.json";
    private final List<Platform> platforms;

    @SneakyThrows
    public PlatformService() {
        this.platforms = JsonUtils.GSON.fromJson(new InputStreamReader(new URL(VERSIONS_URL).openStream()), PlatformConfig.class).platforms();
        log.info("Loading {} platforms with {} versions.", platforms.size(), versionsAmount());
    }

    public Platform find(String platformId) {
        return this.platforms.stream().filter(it -> it.id().equalsIgnoreCase(platformId)).findFirst().orElse(null);
    }

    public int versionsAmount() {
        return this.platforms.stream().mapToInt(it -> it.versions().size()).sum();
    }
}
