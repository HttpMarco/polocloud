package dev.httpmarco.polocloud.suite.dependencies.pool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.suite.PolocloudContext;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.dependencies.external.ExternalDependency;
import dev.httpmarco.polocloud.suite.dependencies.pool.serializer.DependencySerializer;
import dev.httpmarco.polocloud.suite.utils.serializer.VersionSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class DependencyPool {

    private static final Gson POOL_GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Version.class, VersionSerializer.INSTANCE)
            .registerTypeAdapter(ExternalDependency.class, new DependencySerializer())
            .create();

    public static DependencyPool read() {
        try (var inputStream = DependencyPool.class.getClassLoader().getResourceAsStream("dependencies.json")) {

            if(inputStream == null) {
                throw new RuntimeException(PolocloudSuite.instance().translation().get("loading.dependencies.fileNotFound"));
            }

            try (var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                var content = reader.lines().collect(Collectors.joining("\n"));
                // the generated dependency file content
                var pool = POOL_GSON.fromJson(content, JsonArray.class).asList()
                        .stream()
                        .map(it -> POOL_GSON.fromJson(it, ExternalDependency.class))
                        .toList();

                return new DependencyPool(pool);
            }
        } catch (IOException e) {
            throw new RuntimeException(PolocloudSuite.instance().translation().get("loading.dependencies.fileReadFailed"), e);
        }
    }

    private final List<ExternalDependency> dependencies;

    private DependencyPool(List<ExternalDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public List<ExternalDependency> dependencies() {
        return Collections.unmodifiableList(this.dependencies);
    }
}
