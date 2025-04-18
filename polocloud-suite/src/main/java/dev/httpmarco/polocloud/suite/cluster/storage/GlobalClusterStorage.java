package dev.httpmarco.polocloud.suite.cluster.storage;

import dev.httpmarco.polocloud.suite.utils.redis.RedisClient;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public abstract class GlobalClusterStorage<T> implements ClusterStorage<String, T> {

    private final RedisClient redisClient;

    @Override
    public List<T> items() {
        return List.of();
    }

    @Override
    public void destroy(String identifier) {

    }

    @Override
    public T singleton(String identifier) {
        return null;
    }

    @Override
    public void publish(T item) {

    }
}
