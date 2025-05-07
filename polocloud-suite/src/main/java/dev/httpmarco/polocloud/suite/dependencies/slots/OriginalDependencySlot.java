package dev.httpmarco.polocloud.suite.dependencies.slots;

import dev.httpmarco.polocloud.suite.PolocloudContext;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.dependencies.Dependency;
import dev.httpmarco.polocloud.suite.dependencies.DependencySlot;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Getter
@Accessors(fluent = true)
public final class OriginalDependencySlot implements DependencySlot {

    private final List<Dependency> bindDependencies = new ArrayList<>();

    @Override
    public void append(Dependency dependency) {
        try {
            PolocloudContext.attachPath(dependency.file().toPath());
            this.bindDependencies.add(dependency);
        } catch (IOException e) {
            log.error(PolocloudSuite.instance().translation().get("loading.dependencies.attachFailed", dependency.file().toPath()));
            throw new RuntimeException(e);
        }
    }
}
