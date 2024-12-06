package dev.httpmarco.polocloud.api;

import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public final class CloudAPI {

    @Setter
    private static CloudAPI instance;

}
