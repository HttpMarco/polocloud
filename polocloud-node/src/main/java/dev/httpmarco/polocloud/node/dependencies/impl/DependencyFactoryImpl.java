package dev.httpmarco.polocloud.node.dependencies.impl;

import dev.httpmarco.polocloud.node.dependencies.Dependency;
import dev.httpmarco.polocloud.node.dependencies.DependencyFactory;
import dev.httpmarco.polocloud.node.dependencies.validator.ChecksumValidator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public final class DependencyFactoryImpl implements DependencyFactory {

    private static final ChecksumValidator VALIDATOR = new ChecksumValidator();

    @Override
    public void prepare(Dependency dependency) {



    }

    @Override
    public void uninstall(Dependency dependency) {

    }

    @Contract(pure = true)
    @Override
    public @NotNull List<Dependency> loadSubDependencies(Dependency dependency) {
        var dependencies = new ArrayList<Dependency>();



        //todo
        return dependencies;
    }
}
