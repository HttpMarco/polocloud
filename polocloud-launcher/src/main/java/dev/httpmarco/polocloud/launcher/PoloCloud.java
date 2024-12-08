package dev.httpmarco.polocloud.launcher;

import dev.httpmarco.polocloud.launcher.utils.ManifestReader;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.channels.FileLock;

@Getter
@Accessors(fluent = true)
public final class PoloCloud {

    @Getter
    private static PoloCloud launcher;
    @Getter
    private static String version;


    // allow to check if the cloud is running -> Deny multiple instances
    private FileLock polocloudLock;

    public PoloCloud() {
        launcher = this;
        version = ManifestReader.detectVersion();

        // allow us to add more dependencies to the node at runtime
        // todo: implement this lock
        // todo: check version and maybe update this

        var process = new PoloCloudProcess();
        process.start();
    }
}