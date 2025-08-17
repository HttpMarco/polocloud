package dev.httpmarco.polocloud.sdk.java.logger;

import dev.httpmarco.polocloud.shared.logging.Logger;
import dev.httpmarco.polocloud.v1.logger.LoggerControllerGrpc;
import dev.httpmarco.polocloud.v1.logger.LoggerRequest;
import io.grpc.ManagedChannel;
import org.jetbrains.annotations.NotNull;

public class LoggerProvider implements Logger {

    private final LoggerControllerGrpc.LoggerControllerBlockingStub blockingStub;

    public LoggerProvider(ManagedChannel channel) {
        this.blockingStub = LoggerControllerGrpc.newBlockingStub(channel);
    }

    @Override
    public void info(@NotNull String message) {
        this.blockingStub.info(LoggerRequest.newBuilder().setMessage(message).build());
    }

    @Override
    public void warn(@NotNull String message) {
        this.blockingStub.warn(LoggerRequest.newBuilder().setMessage(message).build());
    }

    @Override
    public void error(@NotNull String message) {
        this.blockingStub.error(LoggerRequest.newBuilder().setMessage(message).build());
    }

    @Override
    public void debug(@NotNull String message) {
        this.blockingStub.debug(LoggerRequest.newBuilder().setMessage(message).build());
    }
}
