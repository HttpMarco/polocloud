package dev.httpmarco.polocloud.node.terminal.impl;

import dev.httpmarco.polocloud.node.terminal.NodeTerminal;
import dev.httpmarco.polocloud.node.terminal.NodeTerminalSession;
import dev.httpmarco.polocloud.node.terminal.impl.sessions.SetupTerminalSession;
import dev.httpmarco.polocloud.node.terminal.setup.impl.defaults.OnboardingSetup;
import dev.httpmarco.polocloud.node.terminal.logging.Log4jStream;
import dev.httpmarco.polocloud.node.terminal.utils.TerminalColorReplacer;
import dev.httpmarco.polocloud.node.terminal.utils.TerminalHeader;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
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
public final class JLineNodeTerminalImpl implements NodeTerminal {

    private final Terminal terminal;
    private final LineReaderImpl lineReader;

    private String prompt;
    private NodeTerminalSession<?> session;

    @SneakyThrows
    public JLineNodeTerminalImpl() {

        this.terminal = TerminalBuilder.builder().system(true).encoding(StandardCharsets.UTF_8).dumb(true).jansi(true).build();
        this.updatePrompt("&9default&8@&7cloud &8» &7");

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

        // log default cloud information first
        TerminalHeader.print(this);
    }

    @Override
    public void run() {
        this.resetSession();
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
    public void newSession(NodeTerminalSession<?> session) {
        this.session = session;
        this.session.prepare(terminal);
    }

    @Override
    public void resetSession() {
        this.newSession(new SetupTerminalSession(new OnboardingSetup()));
    }

    public void updatePrompt(String prompt) {
        this.prompt = TerminalColorReplacer.replaceColorCodes(prompt);

        if(lineReader != null) {
            this.lineReader.setPrompt(prompt);
        }
    }

    @Override
    public void displayCursor() {
        printLine("\\u001B[?25h"); // show cursor
    }

    @Override
    public void hideCursor() {
        printLine("\u001B[?25l"); // hide cursor
    }

    @Override
    public NodeTerminal clear() {
        this.terminal.puts(InfoCmp.Capability.clear_screen);
        this.terminal.flush();
        this.update();
        return this;
    }

    public void update() {
        if (!this.lineReader.isReading()) {
            return;
        }
        this.lineReader.callWidget(LineReader.REDRAW_LINE);
        this.lineReader.callWidget(LineReader.REDISPLAY);
    }
}
