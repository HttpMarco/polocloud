package dev.httpmarco.polocloud.node.terminal;

import dev.httpmarco.polocloud.api.Available;
import dev.httpmarco.polocloud.api.Closeable;
import dev.httpmarco.polocloud.node.terminal.commands.CommandService;

public interface Terminal extends Available, Closeable {

    void clear();

    void update();

    void printLine(String message);

    CommandService commandService();

}
