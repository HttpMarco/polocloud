package dev.httpmarco.polocloud.node.dependencies.common;

import dev.httpmarco.polocloud.api.Available;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractDependency implements Available {

    private String groupId;
    private String artifactId;
    private String version;

    @Override
    public boolean available() {
        return groupId != null && artifactId != null && version != null;
    }

    @Contract(pure = true)
    public @NotNull String urlGroupId() {
        return groupId.replace('.', '/');
    }
}
