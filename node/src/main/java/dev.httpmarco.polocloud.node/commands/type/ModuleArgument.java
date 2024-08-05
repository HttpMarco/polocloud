package dev.httpmarco.polocloud.node.commands.type;

import dev.httpmarco.polocloud.node.commands.CommandArgument;
import dev.httpmarco.polocloud.node.commands.CommandContext;
import dev.httpmarco.polocloud.node.module.LoadedModule;
import dev.httpmarco.polocloud.node.module.ModuleProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public final class ModuleArgument extends CommandArgument<LoadedModule> {

    private final ModuleProvider moduleProvider;

    public ModuleArgument(String key, ModuleProvider moduleProvider) {
        super(key);
        this.moduleProvider = moduleProvider;
    }

    @Override
    public boolean predication(@NotNull String rawInput) {
        return this.moduleProvider.getLoadedModules().stream().anyMatch(it -> it.metadata().id().startsWith(rawInput));
    }

    @Contract(pure = true)
    @Override
    public @NotNull String wrongReason() {
        return "The Argument " + key() + " is not a registered module id!";
    }

    @Override
    public @NotNull @Unmodifiable List<String> defaultArgs(CommandContext context) {
        return this.moduleProvider.getLoadedModules().stream().map(it -> it.metadata().id()).toList();
    }

    @Contract("_ -> new")
    @Override
    public LoadedModule buildResult(String input) {
        return this.moduleProvider.getLoadedModules().stream().filter(it -> it.metadata().id().equals(input)).findFirst().orElse(null);
    }
}