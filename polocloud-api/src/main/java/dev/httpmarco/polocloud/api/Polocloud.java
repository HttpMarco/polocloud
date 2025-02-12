package dev.httpmarco.polocloud.api;

public abstract class Polocloud {

    private static Polocloud instance;

    public static Polocloud instance() {
        return instance;
    }
}
