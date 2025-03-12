package dev.httpmarco.polocloud.suite.utils.tasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TickTask implements Task {

    private static final Logger log = LogManager.getLogger(TickTask.class);
    private int tick;
    private final int delay;

    private Thread thread;

    public TickTask(int delay) {
        this.tick = delay;
        this.delay = delay;
    }

    public void stop() {
        if (this.thread != null && !this.thread.isInterrupted()) {
            this.thread.interrupt();
        }
    }

    public void start() {
        this.thread = Thread.ofVirtual().start(() -> {
            while (this.thread.isAlive()) {
                if (tick <= 0) {
                    this.onTick();
                    this.tick = delay;
                    continue;
                }
                tick--;


                try {
                    // we wait one second for the next interval
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("An error occurred while waiting for the next tick", e);
                }
            }
        });
    }
}