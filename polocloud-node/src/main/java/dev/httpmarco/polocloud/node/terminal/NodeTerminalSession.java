package dev.httpmarco.polocloud.node.terminal;

public interface NodeTerminalSession {

    void handleInput(NodeTerminal terminal, String rawLine, String input, String[] args);

}
