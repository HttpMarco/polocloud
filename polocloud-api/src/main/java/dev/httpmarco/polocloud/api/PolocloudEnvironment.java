package dev.httpmarco.polocloud.api;

public enum PolocloudEnvironment {

    POLOCLOUD_SUITE_PORT,
    POLOCLOUD_SUITE_HOSTNAME,
    // path for the running platform jar
    POLOCLOUD_SUITE_PLATFORM_PATH,


    POLOCLOUD_SERVICE_SEPARAT_CLASSLOADER,
    POLOCLOUD_SERVICE_PORT,
    POLOCLOUD_SERVICE_ID;

    public static String read(PolocloudEnvironment environment) {
        return System.getenv(environment.name());
    }

    @Override
    public String toString() {
        return super.name();
    }
}
