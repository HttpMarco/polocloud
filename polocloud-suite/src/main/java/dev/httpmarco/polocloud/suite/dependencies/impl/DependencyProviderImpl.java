package dev.httpmarco.polocloud.suite.dependencies.impl;

import dev.httpmarco.polocloud.suite.dependencies.DependencyProvider;
import dev.httpmarco.polocloud.suite.dependencies.DependencySlot;
import dev.httpmarco.polocloud.suite.dependencies.slots.OriginalDependencySlot;
import dev.httpmarco.polocloud.suite.utils.PathUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DependencyProviderImpl implements DependencyProvider {

    private static final Logger log = LogManager.getLogger(DependencyProviderImpl.class);
    public static final Path DEPENDENCY_DIRECTORY = PathUtils.defineDirectory("local/dependencies");

    private final DependencySlot originalSlot;
    private final List<DependencySlot> slots = new ArrayList<>();

    public DependencyProviderImpl() {
        this.originalSlot = new OriginalDependencySlot();
    }

    @Override
    public DependencySlot original() {
        return this.originalSlot;
    }

    @Override
    public List<DependencySlot> slots() {
        return Collections.unmodifiableList(this.slots);
    }
}
