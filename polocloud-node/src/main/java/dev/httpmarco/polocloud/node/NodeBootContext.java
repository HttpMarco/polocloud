package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.node.utils.ConsoleActions;
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
        // for a better and clean layout -> clean console
        ConsoleActions.cleanLines();
        // start the node
        new Node();
    }
}
