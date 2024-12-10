package dev.httpmarco.polocloud.node.terminal.impl.sessions;

import dev.httpmarco.polocloud.node.terminal.NodeTerminal;
import dev.httpmarco.polocloud.node.terminal.NodeTerminalSession;
import dev.httpmarco.polocloud.node.terminal.setup.Setup;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public final class SetupTerminalSession implements NodeTerminalSession {

    private final Setup setup;

    @Override
    public void handleInput(NodeTerminal terminal, @NotNull String rawLine, String input, String[] args) {
        if (rawLine.equalsIgnoreCase("exit")) {
            setup.exit(false);
            return;
        }

        if (rawLine.equalsIgnoreCase("back")) {
            setup.previousQuestion();
            return;
        }
        setup.answer(rawLine);
    }
}
