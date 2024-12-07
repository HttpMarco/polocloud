package dev.httpmarco.polocloud.launcher;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.channels.FileLock;

@Getter
@Accessors(fluent = true)
public final class PoloCloud {

    @Getter
    public static PoloCloud launcher;
    // allow to check if the cloud is running -> Deny multiple instances
    private FileLock polocloudLock;

    public PoloCloud() {
        launcher = this;
        // allow us to add more dependencies to the node at runtime
        // todo: implement this lock


        var process = new PoloCloudProcess();
        process.start();
    }
}