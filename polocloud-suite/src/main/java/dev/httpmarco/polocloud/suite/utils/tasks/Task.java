package dev.httpmarco.polocloud.suite.utils.tasks;

public interface Task {

    /**
     * This method is called every tick.
     */
    void onTick();

    /**
     * This method is called when the task is started.
     */
    void start();

    /**
     * This method is called when the task is stopped.
     */
    void stop();

}
