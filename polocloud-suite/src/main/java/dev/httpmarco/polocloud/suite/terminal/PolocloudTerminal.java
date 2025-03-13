package dev.httpmarco.polocloud.suite.terminal;

import dev.httpmarco.polocloud.suite.terminal.setup.Setup;
import org.jline.reader.LineReader;

public interface PolocloudTerminal extends AutoCloseable {

    void start();

    void handleInput(String commandName, String[] args);

    String prompt();

    void prompt(String prompt);

    LineReader lineReader();

    void print(String message);

    void refresh();

    void clear();


    // setup methods
    Setup displayedSetup();

    boolean hasSetup();

    void changeSetup(Setup setup);

    default void revertSetup() {
        this.changeSetup(null);
    }
}