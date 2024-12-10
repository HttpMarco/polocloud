package dev.httpmarco.polocloud.node.terminal;

import dev.httpmarco.polocloud.api.Available;
import dev.httpmarco.polocloud.api.Closeable;

public interface NodeTerminal extends Available, Closeable {

    /**
     * Clears the terminal
     */
    void clear();

    /**
     * Prints a message to the terminal
     * @param message the message
     */

    void printLine(String message);

    /**
     * Reads a line from the terminal
     * @return the line
     */
   NodeTerminalSession session();

    /**
     * Creates a new session
     * @param session the session
     */
   void newSession(NodeTerminalSession session);

    /**
     * Resets the session to {@link dev.httpmarco.polocloud.node.terminal.impl.sessions.DefaultTerminalSession}
     */
   void resetSession();

    /**
     * Updates the prompt
     * @param prompt the prompt
     */
   void updatePrompt(String prompt);

}
