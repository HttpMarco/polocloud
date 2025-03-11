package dev.httpmarco.polocloud.suite.cluster.global.syncstorage;

import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuiteData;
import dev.httpmarco.polocloud.suite.utils.GsonInstance;
import dev.httpmarco.polocloud.suite.utils.redis.RedisClient;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ClusterDataSyncStorage implements SyncStorage<ClusterSuiteData> {

    private RedisClient client;
    private String key;

    public ClusterDataSyncStorage(String clusterToken, RedisClient client) {
        this.key = "polocloud-cluster-" + clusterToken;
        this.client = client;
    }

    @Override
    public void push(ClusterSuiteData data) {
        this.client.add(key, GsonInstance.DEFAULT.toJson(data));
    }

    @Override
    public List<ClusterSuiteData> entries() {
        return this.client.list(key).stream().map(s -> GsonInstance.DEFAULT.fromJson(s, ClusterSuiteData.class)).collect(Collectors.toList());
    }

    @Override
    public void update(ClusterSuiteData data) {
        //todo
    }
}
