package dev.httpmarco.polocloud.api;

import dev.httpmarco.polocloud.api.dependencies.DependencyService;
import dev.httpmarco.polocloud.api.logging.Logger;
import dev.httpmarco.polocloud.api.logging.LoggerFactory;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public abstract class CloudAPI {

    @Getter
    private static CloudAPI instance;

    private final LoggerFactory loggerFactory = new LoggerFactory();
    private final Logger logger = new Logger();

    private final DependencyService dependencyService = new DependencyService();

    public CloudAPI() {
        instance = this;
    }
}
