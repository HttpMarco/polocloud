package dev.httpmarco.polocloud.api.platforms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum PlatformType {

    PROXY(25565, "every_proxy", "end"),
    SERVER(20000, "every_server", "stop"),
    SERVER_MASTER(30000, "every_service", null);

    final int defaultRuntimePort;
    final String defaultTemplateSpace;
    final @Nullable String shutdownTypeCommand;

}
