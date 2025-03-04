package dev.httpmarco.polocloud.suite.utils.serializer;

import com.google.gson.*;
import dev.httpmarco.polocloud.suite.configuration.ClusterConfig;

import java.lang.reflect.Type;

public final class SuiteConfigAestheticOptimizationSerializer implements JsonSerializer<ClusterConfig> {

    @Override
    public JsonElement serialize(ClusterConfig src, Type typeOfSrc, JsonSerializationContext context) {
        var data = new JsonObject();

        if (src.clusterToken() != null) {
            data.add("clusterToken", new JsonPrimitive(src.clusterToken()));
        }

        data.add("localSuite", context.serialize(src.localSuite()));
        data.add("externalSuites", context.serialize(src.externalSuites()));

        return data;
    }
}
