package dev.httpmarco.polocloud.node.terminal.impl;

import dev.httpmarco.polocloud.api.Available;
import dev.httpmarco.polocloud.api.Closeable;
import dev.httpmarco.polocloud.node.NodeShutdown;
import lombok.AllArgsConstructor;
import org.jline.reader.UserInterruptException;
import java.util.Arrays;

@AllArgsConstructor
public final class JLineTerminalReadingThread extends Thread implements Closeable, Available {

    private final JLineNodeTerminalImpl terminal;

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                var rawLine = terminal.lineReader().readLine(terminal.prompt()).trim();

                // we don't want to handle empty lines
                if (rawLine.trim().isEmpty()) {
                    continue;
                }

                var line = rawLine.split(" ");
                var commandName = line[0];
                var commandArguments = Arrays.copyOfRange(line, 1, line.length);

                // handle the input with the session
                terminal.session().handleInput(terminal, rawLine, commandName, commandArguments);

            } catch (UserInterruptException exception) {
                // if a command user use strg + c
                NodeShutdown.nodeShutdownTotal(true);
            }
        }
    }

    @Override
    public void close() {
        this.interrupt();
    }

    @Override
    public boolean available() {
        return this.isAlive();
    }
}
