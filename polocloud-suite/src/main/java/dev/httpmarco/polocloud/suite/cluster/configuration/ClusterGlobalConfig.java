package dev.httpmarco.polocloud.suite.cluster.configuration;

import dev.httpmarco.polocloud.suite.cluster.configuration.redis.RedisConfig;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class ClusterGlobalConfig extends ClusterLocalConfig {

    private final String token;
    private final String hostname;
    private final String privateKey;
    private final RedisConfig redis;

    public ClusterGlobalConfig(String id, String hostname, int port, String token, String privateKey, RedisConfig redis) {
        super(id, port);
        this.hostname = hostname;
        this.token = token;
        this.redis = redis;
        this.privateKey = privateKey;
    }
}
