package dev.httpmarco.polocloud.suite.cluster.storage;

import dev.httpmarco.polocloud.suite.utils.GsonInstance;
import dev.httpmarco.polocloud.suite.utils.redis.RedisClient;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class GlobalClusterStorage<T> implements ClusterStorage<String, T> {

    private final Class<T> type;
    private final String storageKey;
    private final RedisClient redisClient;

    @Override
    public List<T> items() {
        return this.redisClient.list(storageKey).stream().map(this::mapStorageItem).toList();
    }

    @Override
    public void destroy(String identifier) {
        this.redisClient.delete(storageKey, identifier);
    }

    @Override
    public T singleton(String identifier) {
        return mapStorageItem(this.redisClient.getSync(identifier));
    }

    @Override
    public void publish(T item) {
        this.redisClient.set(storageKey, extreactIdentifier(item), GsonInstance.DEFAULT.toJson(item));
    }

    private T mapStorageItem(String gson) {
        return GsonInstance.DEFAULT.fromJson(gson, type);
    }
}
