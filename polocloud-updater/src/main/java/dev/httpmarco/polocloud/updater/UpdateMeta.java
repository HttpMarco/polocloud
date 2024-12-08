package dev.httpmarco.polocloud.updater;

import dev.httpmarco.polocloud.launcher.PoloCloud;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateMeta {

    private final String version = PoloCloud.version();
    private String latestGithubRelease;

}
