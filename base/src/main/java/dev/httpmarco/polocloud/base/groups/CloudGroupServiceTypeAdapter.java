package dev.httpmarco.polocloud.base.groups;

import com.google.gson.*;
import dev.httpmarco.osgan.files.Files;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import lombok.SneakyThrows;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CloudGroupServiceTypeAdapter implements JsonSerializer<CloudGroup>, JsonDeserializer<CloudGroup> {

    private static final Path GROUP_FOLDER = Path.of("local/groups");
    private final Gson LOADER = new GsonBuilder().setPrettyPrinting().serializeNulls().registerTypeHierarchyAdapter(CloudGroup.class, this).create();

    public CloudGroupServiceTypeAdapter() {
        Files.createDirectoryIfNotExists(GROUP_FOLDER);
    }

    public void includeFile(CloudGroup cloudGroup) {
        Files.writeString(GROUP_FOLDER.resolve(cloudGroup.name() + ".json"), LOADER.toJson(cloudGroup));
    }

    @SneakyThrows
    public void excludeFile(CloudGroup cloudGroup) {
        java.nio.file.Files.delete(GROUP_FOLDER.resolve(cloudGroup.name() + ".json"));
    }

    public List<CloudGroup> readGroups() {
        var groups = new ArrayList<CloudGroup>();
        for (var file : Objects.requireNonNull(GROUP_FOLDER.toFile().listFiles())) {

            if (!(file.isFile() && file.getName().endsWith(".json"))) {
                continue;
            }

            groups.add(LOADER.fromJson(Files.readString(file.toPath()), CloudGroup.class));
        }
        return groups;
    }

    @Override
    public CloudGroup deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var elements = jsonElement.getAsJsonObject();

        var name = elements.get("name").getAsString();
        var platform = elements.get("platform").getAsString();
        var memory = elements.get("memory").getAsInt();
        var minOnlineServices = elements.get("minOnlineCount").getAsInt();

        return new CloudGroupImpl(name, platform, memory, minOnlineServices);
    }

    @Override
    public JsonElement serialize(CloudGroup cloudGroup, Type type, JsonSerializationContext jsonSerializationContext) {
        var object = new JsonObject();

        object.addProperty("name", cloudGroup.name());
        object.addProperty("platform", cloudGroup.platform());
        object.addProperty("memory", cloudGroup.memory());
        object.addProperty("minOnlineCount", cloudGroup.minOnlineServices());

        var properties = new JsonObject();
        object.add("properties", properties);
        return object;
    }
}
