package dev.httpmarco.polocloud.node.terminal.setup;

import org.jetbrains.annotations.Nullable;

public interface SetupStep {

    Question question();

    @Nullable String answer();

}
