package dev.httpmarco.polocloud.api.logging;

import dev.httpmarco.polocloud.api.CloudAPI;

import java.util.logging.Level;

public final class Logger {

    public void info(String message, Object... objects) {
        this.log(Level.INFO, message, null, objects);
    }

    public void error(String message, Throwable throwable, Object... objects) {
        this.log(Level.SEVERE, message, throwable, objects);
    }

    private void log(Level level, String message, Throwable throwable, Object... objects) {
        CloudAPI.instance().loggerFactory().print(level, message, throwable, objects);
    }
}
