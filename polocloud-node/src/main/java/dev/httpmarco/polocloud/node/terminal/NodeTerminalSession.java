package dev.httpmarco.polocloud.node.terminal;

import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;

public interface NodeTerminalSession<R> {

    /**
     * Describe the wait process
     * @param lineReader the line reader
     */
    R waitFor(LineReaderImpl lineReader);

    /**
     * Handle the input
     * @param result the result
     */
    void handleInput(R result);

    /**
     * Check if the answer is valid
     * @param result the result
     * @return if the answer is valid
     */
    default boolean codecAnswer(R result) {
        return true;
    }

    /**
     * Optional method to prepare the terminal
     * @param terminal the terminal
     */
    default void prepare(Terminal terminal) {}
}
