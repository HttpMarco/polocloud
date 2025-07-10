package dev.httpmarco.polocloud.launcher.lib;

public class PolocloudLibNotFoundException extends RuntimeException {

    public PolocloudLibNotFoundException(String lib) {
        super("Lib not found: " + lib);

    }
}
