package dev.httpmarco.polocloud.base.terminal;

import dev.httpmarco.polocloud.base.CloudBase;
import org.fusesource.jansi.Ansi;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;

import java.util.logging.Level;

public final class CloudTerminalThread extends Thread {

    private final String prompt;
    private final CloudTerminal terminal;

    public CloudTerminalThread(CloudTerminal terminal) {
        this.terminal = terminal;

        setName("console-reading-thread");
        this.prompt = this.terminal.includeColorCodes("&3cloud &2» &1");
        setContextClassLoader(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                try {
                    try {
                        final var line = terminal.lineReader().readLine(prompt).split(" ");
                        resetConsoleInput();

                        if (line.length > 0) {
                            terminal.commandService().call(line);
                        }
                    } catch (EndOfFileException ignore) {
                        resetConsoleInput();
                    }
                } catch (UserInterruptException exception) {
                    resetConsoleInput();
                    CloudBase.instance().shutdown(false);
                }
            } catch (Exception e) {
                resetConsoleInput();
                e.printStackTrace();
            }
        }
    }

    private void resetConsoleInput() {
        this.terminal.print(Level.OFF, Ansi.ansi().reset().cursorUp(1).eraseLine().toString(), null);
    }
}
