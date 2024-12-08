package dev.httpmarco.polocloud.node.terminal.logging;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public class Log4jStream extends ByteArrayOutputStream {

    private final LoggingCallback callback;

    @Override
    public void flush() throws IOException {
        super.flush();

        var input = this.toString(StandardCharsets.UTF_8);
        super.reset();

        if (input != null && !input.isEmpty()) {
            callback.print(input.replace("\n", ""));
        }
    }

    @Contract(" -> new")
    public @NotNull PrintStream printStream() {
        return new PrintStream(this, true, StandardCharsets.UTF_8);
    }
}