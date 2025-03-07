package dev.httpmarco.polocloud.suite.cluster.configuration.serializer;

import com.google.gson.*;
import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuiteData;
import dev.httpmarco.polocloud.suite.cluster.global.ExternalSuite;

import java.lang.reflect.Type;

public final class ExternalSuiteSerializer implements JsonSerializer<ExternalSuite>, JsonDeserializer<ExternalSuite> {

    @Override
    public ExternalSuite deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var data = json.getAsJsonObject();

        var id = data.get("id").getAsString();
        var hostname = data.get("hostname").getAsString();
        var privateKey = data.get("privateKey").getAsString();
        var port = data.get("port").getAsInt();

        return new ExternalSuite(new ClusterSuiteData(id, hostname, privateKey, port));
    }

    @Override
    public JsonElement serialize(ExternalSuite src, Type typeOfSrc, JsonSerializationContext context) {
        var data = new JsonObject();

        data.addProperty("id", src.data().id());
        data.addProperty("hostname", src.data().hostname());
        data.addProperty("port", src.data().port());

        return data;
    }
}
