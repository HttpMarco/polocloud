package dev.httpmarco.polocloud.base.terminal;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.logging.LogLevel;
import dev.httpmarco.polocloud.api.properties.CloudProperty;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import org.fusesource.jansi.Ansi;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;

public final class CloudTerminalThread extends Thread {
    private final String prompt;
    private final CloudTerminal terminal;

    public CloudTerminalThread(CloudTerminal terminal) {
        this.terminal = terminal;

        setName("console-reading-thread");

        var globalProperties = CloudAPI.instance().globalProperties();
        this.prompt = this.terminal.includeColorCodes(globalProperties.has(CloudProperty.PROMPT) ? globalProperties.property(CloudProperty.PROMPT) : "&3cloud &2» &1");

        setContextClassLoader(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                try {
                    try {
                        final var rawLine = terminal.lineReader().readLine(prompt);
                        final var line = rawLine.split(" ");
                        resetConsoleInput();

                        if (line.length > 0) {
                            var service = CloudAPI.instance().serviceProvider().services()
                                    .stream()
                                    .map(it -> (LocalCloudService) it)
                                    .filter(LocalCloudService::subscribed).findFirst()
                                    .orElse(null);
                            if(service != null) {
                                if(rawLine.equalsIgnoreCase("leave")) {
                                    service.subscribeLog();
                                    this.terminal.clear();
                                } else {
                                    service.execute(rawLine);
                                }
                            } else {
                                terminal.commandService().call(line);
                            }
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
        this.terminal.print(LogLevel.OFF, Ansi.ansi().reset().cursorUp(1).eraseLine().toString(), null);
    }
}
