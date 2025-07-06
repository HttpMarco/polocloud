package dev.httpmarco.polocloud.launcher;

public class PolocloudLibNotFoundException extends RuntimeException {

    public PolocloudLibNotFoundException(String lib) {
        super("Lib not found: " + lib);

    }
}
