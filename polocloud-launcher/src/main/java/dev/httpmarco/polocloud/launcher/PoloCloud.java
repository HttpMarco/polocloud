package dev.httpmarco.polocloud.launcher;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.channels.FileLock;

@Getter
@Accessors(fluent = true)
public final class PoloCloud {

    // allow to check if the cloud is running -> Deny multiple instances
    // todo: implement this
    private FileLock polocloudLock;

    private final PoloCloudLoader loader;

    public PoloCloud() {
        // allow us to add more dependencies to the node at runtime
        this.loader = new PoloCloudLoader();
        // append the access for the current thread
        Thread.currentThread().setContextClassLoader(this.loader);
    }
}