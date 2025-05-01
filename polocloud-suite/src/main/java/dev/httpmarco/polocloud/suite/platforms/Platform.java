package dev.httpmarco.polocloud.suite.platforms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class Platform {

    private final String name;
    private final String icon;
    private final PlatformType type;
    private final PlatformLanguage language;
    private final String url;

    private final List<PlatformVersion> versions;

}