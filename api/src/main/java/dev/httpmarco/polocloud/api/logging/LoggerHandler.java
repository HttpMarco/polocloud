package dev.httpmarco.polocloud.api.logging;

public interface LoggerHandler {

    void print(LogLevel level, String message, Throwable throwable, Object... objects);

    void close();

}