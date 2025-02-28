package dev.httpmarco.polocloud.suite.terminal;

import dev.httpmarco.polocloud.component.ComponentSuite;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import org.jline.reader.UserInterruptException;

import java.util.Arrays;

final class PolocloudTerminalThread extends Thread {

    private final PolocloudTerminal terminal;

    public PolocloudTerminalThread(PolocloudTerminal terminal) {
        super("Polocloud-Terminal-Thread");
        this.terminal = terminal;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            String line;

            try {
                line = terminal.lineReader().readLine(terminal.prompt());
            } catch (UserInterruptException ignored) {
                // user pressed ctrl+c
                PolocloudSuite.instance().shutdown();
                return;
            }

            if (line.isBlank()) {
                continue;
            }

            String[] splat = line.split(" ");
            String commandName = splat[0];
            String[] args = Arrays.copyOfRange(splat, 1, splat.length);

            this.terminal.handleInput(commandName, args);
        }
    }

}