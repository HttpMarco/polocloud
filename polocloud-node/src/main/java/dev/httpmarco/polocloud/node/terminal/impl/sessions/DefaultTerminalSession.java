package dev.httpmarco.polocloud.node.terminal.impl.sessions;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.terminal.NodeTerminal;
import dev.httpmarco.polocloud.node.terminal.NodeTerminalSession;

/**
 * This class is an implementation of {@link NodeTerminalSession} and is used to handle the input of the terminal
 * Default implementation, log lines and execute commands
 */
public final class DefaultTerminalSession implements NodeTerminalSession {

    public static final DefaultTerminalSession INSTANCE = new DefaultTerminalSession();

    @Override
    public void handleInput(NodeTerminal terminal, String rawLine, String input, String[] args) {
        Node.instance().commandService().call(input, args);
    }
}
