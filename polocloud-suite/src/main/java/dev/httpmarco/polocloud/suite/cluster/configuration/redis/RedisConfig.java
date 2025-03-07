package dev.httpmarco.polocloud.suite.cluster.configuration.redis;

public record RedisConfig(String hostname, int port, String username, String password, int database) {


}
