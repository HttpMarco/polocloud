package dev.httpmarco.polocloud.node.terminal.impl;

import dev.httpmarco.polocloud.api.Closeable;
import dev.httpmarco.polocloud.node.terminal.utils.TerminalColorReplacer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class JLineTerminalReadingThread extends Thread implements Closeable {

    private final String prompt = TerminalColorReplacer.replaceColorCodes("&9default&8@&7cloud &8» &7");
    private final JLineTerminalImpl terminal;

    @Override
    public void run() {
        while (!isInterrupted()) {
            var rawLine = terminal.lineReader().readLine(prompt).trim();



        }
    }

    @Override
    public void close() {
        this.interrupt();
    }
}
