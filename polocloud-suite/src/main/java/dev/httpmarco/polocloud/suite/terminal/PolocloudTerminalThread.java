package dev.httpmarco.polocloud.suite.terminal;

import dev.httpmarco.polocloud.suite.PolocloudShutdownHandler;
import org.jline.jansi.Ansi;
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
                PolocloudShutdownHandler.shutdown();
                return;
            }

            if (line.isBlank()) {
                // we reset the terminal prompt as message -> we have a clean console
                System.out.println(Ansi.ansi().cursorUpLine().eraseLine().toString() + Ansi.ansi().cursorUp(1).toString());
                continue;
            }

            String[] splat = line.split(" ");
            String commandName = splat[0];
            String[] args = Arrays.copyOfRange(splat, 1, splat.length);

            this.terminal.handleInput(commandName, args);
        }
    }

}