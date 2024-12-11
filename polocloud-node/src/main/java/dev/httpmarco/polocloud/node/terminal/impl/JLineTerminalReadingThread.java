package dev.httpmarco.polocloud.node.terminal.impl;

import dev.httpmarco.polocloud.api.Available;
import dev.httpmarco.polocloud.api.Closeable;
import dev.httpmarco.polocloud.node.NodeShutdown;
import dev.httpmarco.polocloud.node.terminal.NodeTerminalSession;
import lombok.AllArgsConstructor;
import org.jline.reader.UserInterruptException;
@AllArgsConstructor
public final class JLineTerminalReadingThread extends Thread implements Closeable, Available {

    private final JLineNodeTerminalImpl terminal;

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                NodeTerminalSession session = terminal.session();
                var result = session.waitFor(terminal.lineReader());

                if (session.codecAnswer(result)) {
                    session.handleInput(result);
                }
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
