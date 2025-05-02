package dev.httpmarco.polocloud.suite.terminal;

import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.global.GlobalCluster;
import dev.httpmarco.polocloud.suite.terminal.setup.Setup;
import dev.httpmarco.polocloud.suite.utils.ConsoleActions;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class PolocloudTerminalImpl implements PolocloudTerminal {

    private Terminal terminal;
    private LineReaderImpl lineReader;
    private PolocloudTerminalThread terminalThread;

    private String terminalPrompt;
    private Setup displayedSetup;

    @Override
    public void start() {

        try {
            terminal = TerminalBuilder.builder()
                    .system(true)
                    .encoding(StandardCharsets.UTF_8)
                    .dumb(true)
                    .jansi(true)
                    .build();
        } catch (IOException ex) {
            throw new RuntimeException("Cannot build Terminal!", ex);
        }

        this.lineReader = (LineReaderImpl) LineReaderBuilder.builder()
                .terminal(this.terminal)
                .completer(new PolocloudTerminalCompleter())
                .option(LineReader.Option.AUTO_MENU_LIST, true)
                .variable(LineReader.COMPLETION_STYLE_LIST_SELECTION, "fg:cyan")
                .variable(LineReader.COMPLETION_STYLE_LIST_BACKGROUND, "fg:default")
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .option(LineReader.Option.AUTO_PARAM_SLASH, false)
                .variable(LineReader.BELL_STYLE, "none")
                .build();

        this.refresh();
        (terminalThread = new PolocloudTerminalThread(this)).start();
    }

    @Override
    public void close() throws Exception {
        terminal.close();
        terminal = null;

        lineReader = null;

        terminalThread.interrupt();
        terminalThread = null;
    }

    @Override
    public void handleInput(String commandName, String[] args) {

        if(displayedSetup != null) {
            displayedSetup.answer(commandName);
            return;
        }

        PolocloudSuite.instance().commandService().call(commandName, args);
    }

    @Override
    public void print(String message) {
        if (terminal == null) {
            // we wait here for the right terminal
            System.out.println(LoggingColors.translate(message));
            return;
        }

        this.terminal.puts(InfoCmp.Capability.carriage_return);
        this.terminal.writer().println(LoggingColors.translate(message));
        this.update();
    }

    @Override
    public String prompt() {
        return this.terminalPrompt;
    }

    @Override
    public void prompt(String prompt) {
        this.lineReader.setPrompt(prompt);
    }

    @Override
    public LineReader lineReader() {
        return lineReader;
    }

    private void update() {
        if (this.lineReader.isReading()) {
            this.lineReader.callWidget(LineReader.REDRAW_LINE);
            this.lineReader.callWidget(LineReader.REDISPLAY);
        }
    }

    @Override
    public void refresh() {
        var cluster = PolocloudSuite.instance().cluster();
        var prompt = "&flocal&8@&7suite &8» &7";

        if (cluster instanceof GlobalCluster globalCluster) {
            prompt = "&fglobal&8@&7" + globalCluster.localSuite().id() + " &8» &7";
        }

        this.terminalPrompt = LoggingColors.translate(prompt);
        this.prompt(this.terminalPrompt);
    }

    @Override
    public void clear() {
        ConsoleActions.clearScreen();
    }

    @Override
    public Setup displayedSetup() {
        return displayedSetup;
    }

    @Override
    public boolean hasSetup() {
        return this.displayedSetup != null;
    }

    @Override
    public void changeSetup(Setup setup) {
        this.displayedSetup = setup;
    }
}
