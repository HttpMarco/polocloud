package dev.httpmarco.polocloud.api.logging;

import dev.httpmarco.polocloud.api.CloudAPI;

public final class Logger {

    public void info(String message, Object... objects) {
        this.log(LogLevel.INFO, message, null, objects);
    }

    public void error(String message, Throwable throwable, Object... objects) {
        this.log(LogLevel.ERROR, message, throwable, objects);
    }

    public void success(String message, Object... objects) {
        this.log(LogLevel.SUCCESS, message, null, objects);
    }

    public void warn(String message, Object... objects) {
        this.log(LogLevel.WARN, message, null, objects);
    }


    private void log(LogLevel level, String message, Throwable throwable, Object... objects) {
        CloudAPI.instance().loggerFactory().print(level, message, throwable, objects);
    }
}
