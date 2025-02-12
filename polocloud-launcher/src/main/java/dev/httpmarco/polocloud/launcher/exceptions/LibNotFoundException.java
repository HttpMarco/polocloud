package dev.httpmarco.polocloud.launcher.exceptions;

public final class LibNotFoundException extends Throwable {

    public LibNotFoundException(String name) {
        super("lib not found: " + name);
    }
}
