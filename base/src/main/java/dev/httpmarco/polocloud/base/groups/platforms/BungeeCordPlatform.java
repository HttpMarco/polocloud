package dev.httpmarco.polocloud.base.groups.platforms;

import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import lombok.SneakyThrows;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

public class BungeeCordPlatform extends Platform {

    private static final String LATEST_BUNGEECORD = "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar";

    public BungeeCordPlatform() {
        super(true);

        possibleVersions().add(new PlatformVersion("bungeecord-latest", proxy()));
    }

    @Override
    @SneakyThrows
    public void download(String version) {
        final var url = URI.create(LATEST_BUNGEECORD).toURL();
        //todo duplicated code
        var platformPath = Path.of("local").resolve("platforms").resolve(version);
        platformPath.toFile().mkdirs();

        Files.copy(url.openConnection().getInputStream(), Path.of(platformPath + "/server.jar"));
    }

    @Override
    public void prepare(LocalCloudService localCloudService) {
        //todo not used
    }
}
