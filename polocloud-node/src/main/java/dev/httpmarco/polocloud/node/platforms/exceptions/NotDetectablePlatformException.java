package dev.httpmarco.polocloud.node.platforms.exceptions;

public final class NotDetectablePlatformException extends RuntimeException {

    public NotDetectablePlatformException(String platform, String version) {
        super("The platform " + platform + " with version " + version + " could not be detected! Look in your platforms.json!");
    }
}
