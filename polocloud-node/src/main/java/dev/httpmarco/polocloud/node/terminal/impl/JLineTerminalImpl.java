package dev.httpmarco.polocloud.node.terminal.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.nio.charset.StandardCharsets;

@Accessors(fluent = true)
public final class JLineTerminalImpl implements dev.httpmarco.polocloud.node.terminal.Terminal {

    private final Terminal terminal;
    @Getter(AccessLevel.PACKAGE)
    private final LineReaderImpl lineReader;

    @SneakyThrows
    public JLineTerminalImpl() {
        this.terminal = TerminalBuilder.builder()
                .system(true)
                .encoding(StandardCharsets.UTF_8)
                .dumb(true)
                .jansi(true)
                .build();

        this.lineReader = (LineReaderImpl) LineReaderBuilder.builder()
                .terminal(terminal)
                //  .completer(new JLineTerminalCompleter())

                .option(LineReader.Option.AUTO_MENU_LIST, true)
                // change color of selection box
                .variable(LineReader.COMPLETION_STYLE_LIST_SELECTION, "fg:cyan")
                .variable(LineReader.COMPLETION_STYLE_LIST_BACKGROUND, "bg:default")

                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .option(LineReader.Option.AUTO_PARAM_SLASH, false)
                .variable(LineReader.BELL_STYLE, "none")
                .build();

        this.clear();
        new JLineTerminalReadingThread(this).start();
    }

    @Override
    public boolean available() {
        return terminal.echo();
    }

    @Override
    @SneakyThrows
    public void close() {
        this.terminal.close();
    }

    @Override
    public void clear() {
        this.terminal.puts(InfoCmp.Capability.clear_screen);
        this.terminal.flush();
        this.update();
    }

    @Override
    public void update() {
        if (!this.lineReader.isReading()) {
            return;
        }
        this.lineReader.callWidget(LineReader.REDRAW_LINE);
        this.lineReader.callWidget(LineReader.REDISPLAY);
    }
}
