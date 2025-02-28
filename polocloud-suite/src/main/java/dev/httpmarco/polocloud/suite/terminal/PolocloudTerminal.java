package dev.httpmarco.polocloud.suite.terminal;

import org.jline.reader.LineReader;

public interface PolocloudTerminal extends AutoCloseable {

    void start();

    void handleInput(String commandName, String[] args);

    String prompt();

    void prompt(String prompt);

    LineReader lineReader();
}