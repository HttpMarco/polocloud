package dev.httpmarco.polocloud.modules.rest.configuration;

import dev.httpmarco.polocloud.node.util.JsonUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Accessors(fluent = true)
public class Config {

    public static final Path CONFIG_PATH = Path.of("./local/modules/rest/config.json");
    public static final Path USERS_PATH = Path.of("./local/modules/rest/users.json");
    private JavalinConfiguration javalinConfiguration;
    private UsersConfiguration usersConfiguration;

    @SneakyThrows
    public Config() {
        startupJavalinConfiguration();
        startupUsersConfiguration();
    }

    @SneakyThrows
    private void startupUsersConfiguration() {
        if (!Files.exists(USERS_PATH)) {
            if (!Files.exists(USERS_PATH.getParent())) {
                Files.createDirectory(USERS_PATH.getParent());
            }
            Files.createFile(USERS_PATH);
            this.usersConfiguration = new UsersConfiguration();

            save(this.usersConfiguration, USERS_PATH);
            return;
        }

        this.usersConfiguration = load(UsersConfiguration.class, USERS_PATH);
    }

    @SneakyThrows
    private void startupJavalinConfiguration() {
        if (!Files.exists(CONFIG_PATH)) {
            if (!Files.exists(CONFIG_PATH.getParent())) {
                Files.createDirectory(CONFIG_PATH.getParent());
            }
            Files.createFile(CONFIG_PATH);
            this.javalinConfiguration = new JavalinConfiguration();

            save(this.javalinConfiguration, CONFIG_PATH);
            return;
        }

        this.javalinConfiguration = load(JavalinConfiguration.class, CONFIG_PATH);
    }

    @SneakyThrows
    public static <T> T load(Class<T> clazz, Path path) {
        return JsonUtils.GSON.fromJson(Files.readString(path), clazz);
    }

    @SneakyThrows
    public static <T> void save(T config, Path path) {
        Files.writeString(path, JsonUtils.GSON.toJson(config));
    }
}