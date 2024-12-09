package dev.httpmarco.polocloud.node.terminal.impl;

import dev.httpmarco.polocloud.node.terminal.commands.CommandService;
import dev.httpmarco.polocloud.node.terminal.logging.Log4jStream;
import dev.httpmarco.polocloud.node.terminal.setup.Setup;
import dev.httpmarco.polocloud.node.terminal.utils.TerminalColorReplacer;
import dev.httpmarco.polocloud.node.terminal.utils.TerminalHeader;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;
import org.jline.jansi.Ansi;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.nio.charset.StandardCharsets;

@Log4j2
@Getter
@Accessors(fluent = true)
public final class JLineTerminalImpl implements dev.httpmarco.polocloud.node.terminal.Terminal {

    private final Terminal terminal;
    @Getter(AccessLevel.PACKAGE)
    private final LineReaderImpl lineReader;
    private final CommandService commandService;
    private @Nullable Setup setup;

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

        System.setOut(new Log4jStream(this::printLine).printStream());
        System.setErr(new Log4jStream(log::error).printStream());

        this.clear();
        this.commandService = new CommandService();

        // log default cloud information first
        TerminalHeader.print(this);
        new JLineTerminalReadingThread(this).start();
    }

    @Override
    public boolean available() {
        return true;
    }

    @Override
    @SneakyThrows
    public void close() {
        this.terminal.close();
    }

    public void printLine(String message) {
        this.terminal.puts(InfoCmp.Capability.carriage_return);
        this.terminal.writer().println(TerminalColorReplacer.replaceColorCodes(message) + Ansi.ansi().a(Ansi.Attribute.RESET).toString());
        this.terminal.flush();
        this.update();
    }

    @Override
    public void setup(Setup setup) {
        this.setup = setup;
    }

    @Override
    public void updatePrompt(String prompt) {
        this.lineReader.setPrompt(TerminalColorReplacer.replaceColorCodes(prompt));
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
