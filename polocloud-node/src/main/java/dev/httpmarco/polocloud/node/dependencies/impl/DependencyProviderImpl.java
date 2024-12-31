package dev.httpmarco.polocloud.node.dependencies.impl;

import dev.httpmarco.polocloud.node.dependencies.DependencyProvider;
import dev.httpmarco.polocloud.node.dependencies.DependencySlot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class DependencyProviderImpl implements DependencyProvider {

    private final DependencySlot originalSlot = new DependencySlot();
    private final List<DependencySlot> slots = new ArrayList<>();

}
