package dev.httpmarco.polocloud.suite.cluster.configuration.serializer;

import com.google.gson.*;
import dev.httpmarco.polocloud.suite.cluster.ClusterConfig;
import dev.httpmarco.polocloud.suite.cluster.configuration.ClusterGlobalConfig;
import dev.httpmarco.polocloud.suite.cluster.configuration.ClusterLocalConfig;
import dev.httpmarco.polocloud.suite.cluster.configuration.redis.RedisConfig;

import java.lang.reflect.Type;

public final class ClusterConfigSerializer implements JsonSerializer<ClusterConfig>, JsonDeserializer<ClusterConfig> {

    @Override
    public ClusterConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var data = json.getAsJsonObject();

        String suiteId = data.get("suiteId").getAsString();
        int port = data.get("port").getAsInt();

        if (data.has("token")) {
            return new ClusterGlobalConfig(suiteId,
                    data.get("hostname").getAsString(),
                    port, data.get("token").getAsString(),
                    data.get("privateKey").getAsString(),
                    context.deserialize(data.get("redis"), RedisConfig.class)
            );
        }
        return new ClusterLocalConfig(suiteId, port);
    }

    @Override
    public JsonElement serialize(ClusterConfig src, Type typeOfSrc, JsonSerializationContext context) {
        var data = new JsonObject();

        if (src instanceof ClusterGlobalConfig globalConfig) {
            data.addProperty("token", globalConfig.token());
            data.addProperty("hostname", globalConfig.hostname());
            data.addProperty("privateKey", globalConfig.privateKey());
            data.add("redis", context.serialize(globalConfig.redis()));
        }

        data.addProperty("suiteId", src.id());
        data.addProperty("port", src.port());
        return data;
    }
}
