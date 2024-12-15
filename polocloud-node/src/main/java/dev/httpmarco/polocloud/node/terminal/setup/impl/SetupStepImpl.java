package dev.httpmarco.polocloud.node.terminal.setup.impl;

import dev.httpmarco.polocloud.node.terminal.setup.Question;
import dev.httpmarco.polocloud.node.terminal.setup.SetupStep;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class SetupStepImpl implements SetupStep {

    private final Question question;
    private @Nullable String answer;

}
