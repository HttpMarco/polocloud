package dev.httpmarco.polocloud.suite.configuration;

public record RedisConfig(String hostname, int port, String database, String password, String username) {

}
