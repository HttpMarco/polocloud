package dev.httpmarco.polocloud.suite.utils.redis;

import dev.httpmarco.polocloud.api.Available;
import dev.httpmarco.polocloud.suite.cluster.configuration.ClusterGlobalConfig;
import dev.httpmarco.polocloud.suite.cluster.configuration.redis.RedisConfig;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class RedisClient implements Available {

    private static final Logger log = LogManager.getLogger(RedisClient.class);
    private StatefulRedisConnection<String, String> connection;

    public RedisClient(RedisConfig config) {
        try {
            var client = io.lettuce.core.RedisClient.create(RedisURI.builder()
                    .withHost(config.hostname())
                    .withPort(config.port())
                    .withPassword(config.password().toCharArray())
                    .withDatabase(config.database())
                    .build());

            this.connection = client.connect();
        }catch (Exception e) {
            log.warn("Could not connect to redis server! Please check credentials and server!");
        }
    }

    public CompletableFuture<String> get(String key) {
        return this.connection.async().get(key).toCompletableFuture();
    }

    public String getSync(String key) {
        return this.connection.sync().get(key);
    }

    public void add(String key, String object) {
        RedisCommands<String, String> redis = connection.sync();
        redis.sadd(key, object);
    }

    public void delete(String key, String object) {
        RedisCommands<String, String> redis = connection.sync();
        redis.srem(key, object);
    }

    public Set<String> list(String key) {
        RedisCommands<String, String> redis = connection.sync();
        return redis.smembers(key);
    }

    @Override
    public boolean available() {
        return this.connection != null && this.connection.isOpen();
    }
}