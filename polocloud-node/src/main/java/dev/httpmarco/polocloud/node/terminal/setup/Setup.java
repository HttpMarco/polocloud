package dev.httpmarco.polocloud.node.terminal.setup;

import dev.httpmarco.polocloud.api.Closeable;

public interface Setup extends Closeable {

    /**
     * The display id of this setup
     * @return the id
     */
    String id();

    /**
     * The current step
     * @return the step
     */
    SetupStep current();

    /**
     * Alert the next step
     */
    void next();

    /**
     * Alert the previous step
     */
    void previous();

    /**
     * Start the setup
     */
    void start();

    /**
     * Display the current setup
     */
    void redisplay();

}
