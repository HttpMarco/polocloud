package dev.httpmarco.polocloud.node.terminal.impl.sessions;

import dev.httpmarco.polocloud.node.terminal.NodeTerminal;
import dev.httpmarco.polocloud.node.terminal.NodeTerminalSession;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public final class SetupTerminalSession implements NodeTerminalSession {

    @Override
    public void handleInput(NodeTerminal terminal, @NotNull String rawLine, String input, String[] args) {

    }
}
