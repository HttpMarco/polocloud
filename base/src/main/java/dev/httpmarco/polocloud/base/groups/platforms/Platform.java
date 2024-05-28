package dev.httpmarco.polocloud.base.groups.platforms;

import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class Platform {

    private final boolean proxy;
    private final Set<PlatformVersion> possibleVersions = new HashSet<>();

    public abstract void download(String version);

    public abstract void prepare(LocalCloudService localCloudService);

    public String[] platformsEnvironment() {
        return new String[0];
    }

    public String[] platformsArguments() {
        return new String[0];
    }
}