package dev.httpmarco.polocloud.component.terminal;

import dev.httpmarco.polocloud.component.api.ComponentSuite;
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
            String line = "";
            try {
                line = terminal.lineReader().readLine(terminal.prompt());
            } catch (UserInterruptException ignored) {
                // user pressed ctrl+c
                ComponentSuite.instance().suiteSystemProvider().shutdown();
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