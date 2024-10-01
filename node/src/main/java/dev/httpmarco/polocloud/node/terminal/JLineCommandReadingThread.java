package dev.httpmarco.polocloud.node.terminal;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.NodeConfig;
import dev.httpmarco.polocloud.node.NodeShutdown;
import dev.httpmarco.polocloud.node.terminal.util.TerminalColorUtil;
import lombok.extern.slf4j.Slf4j;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;

import java.util.Arrays;

@Slf4j
public final class JLineCommandReadingThread extends Thread {

    private final NodeConfig localNodeImpl;
    private final JLineTerminal terminal;


    public JLineCommandReadingThread(NodeConfig localNodeImpl, JLineTerminal terminal) {
        this.localNodeImpl = localNodeImpl;
        this.terminal = terminal;

        setContextClassLoader(ClassLoader.getSystemClassLoader());
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                try {
                    try {
                        var rawLine = terminal.lineReader().readLine(TerminalColorUtil.replaceColorCodes(prompt())).trim();

                        if (rawLine.isEmpty()) {
                            continue;
                        }

                        final var line = rawLine.split(" ");

                        if (line.length > 0) {
                            if (terminal.hasSetup()) {

                                if (rawLine.equalsIgnoreCase("exit")) {
                                    terminal.setup().exit(false);
                                    continue;
                                }

                                if (rawLine.equalsIgnoreCase("back")) {
                                    terminal.setup().previousQuestion();
                                    continue;
                                }

                                terminal.setup().answer(rawLine);

                            } else if (Node.instance().screenProvider().isUsed()) {
                                if (rawLine.equalsIgnoreCase("exit") || rawLine.equalsIgnoreCase("leave")) {
                                    Node.instance().screenProvider().redraw();
                                } else {
                                    Node.instance().screenProvider().current().executeCommand(rawLine);
                                }
                            } else {
                                Node.instance().commandService().call(line[0], Arrays.copyOfRange(line, 1, line.length));
                            }
                        }
                    } catch (EndOfFileException ignore) {
                    }
                } catch (UserInterruptException exception) {
                    // if a command user use strg + c
                    NodeShutdown.nodeShutdownTotal(true);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private String prompt() {
        if(terminal.hasSetup()) {
            return "&8» &7";
        }
        if(Node.instance().screenProvider().isUsed()) {
            return "&9" + Node.instance().screenProvider().current().name() + " &8» &7";
        }

        return "&9" + localNodeImpl.localNode().name() + "&8@&7cloud &8» &7";
    }

}
