package dev.httpmarco.polocloud.api.platforms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum PlatformType {

    PROXY(25565, "every_proxy"),
    SERVER(20000, "every_server"),
    SERVER_MASTER(30000, "every_service");

    final int defaultRuntimePort;
    final String defaultTemplateSpace;

}
