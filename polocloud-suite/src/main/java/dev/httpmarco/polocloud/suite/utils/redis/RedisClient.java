package dev.httpmarco.polocloud.suite.utils.redis;

import dev.httpmarco.polocloud.suite.configuration.RedisConfig;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

import java.util.concurrent.CompletableFuture;

public final class RedisClient {

    private final StatefulRedisConnection<String, String> connection;

    public RedisClient(RedisConfig config) {
        var client = io.lettuce.core.RedisClient.create(RedisURI.builder()
                .withHost(config.hostname())
                .withPort(config.port())
                .withPassword(config.password().toCharArray())
                .build());
        this.connection = client.connect();
    }

    public CompletableFuture<String> get(String key) {
        return this.connection.async().get(key).toCompletableFuture();
    }

    public String getSync(String key) {
        return this.connection.sync().get(key);
    }
}
