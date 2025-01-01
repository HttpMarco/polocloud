package dev.httpmarco.polocloud.node;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.lang.instrument.Instrumentation;

@Accessors(fluent = true)
public final class NodeBootContext {

    @Getter
    private static Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    public static void main(String[] args) {
        // todo random shit here
        new Node();
    }
}
