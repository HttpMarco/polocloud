package dev.httpmarco.polocloud.node.terminal.impl.sessions;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.terminal.NodeTerminalSession;
import org.jetbrains.annotations.NotNull;
import org.jline.reader.impl.LineReaderImpl;

import java.util.Arrays;

public final class DefaultTerminalSession implements NodeTerminalSession<String> {

    public static DefaultTerminalSession INSTANCE = new DefaultTerminalSession();

    @Override
    public @NotNull String waitFor(@NotNull LineReaderImpl lineReader) {
        return lineReader.readLine(Node.instance().terminal().prompt()).trim();
    }

    @Override
    public void handleInput(@NotNull String result) {
        var line = result.split(" ");
        var commandName = line[0];
        var commandArguments = Arrays.copyOfRange(line, 1, line.length);

        Node.instance().commandService().call(commandName, commandArguments);
    }

    @Override
    public boolean codecAnswer(@NotNull String result) {
        // we don't want to handle empty lines
       return !result.trim().isEmpty();
    }
}
