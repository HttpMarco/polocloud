package dev.httpmarco.polocloud.suite.utils.tasks;

public interface Task {

    void onTick();

    void start();

    void stop();

}
