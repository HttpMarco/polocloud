package dev.httpmarco.polocloud.suite.platforms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class Platform {

    private final String name;
    private final String icon;
    private final PlatformType type;
    private final PlatformLanguage language;
    private String url;

}
