package dev.httpmarco.polocloud.node.boot;

import dev.httpmarco.polocloud.node.Node;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.lang.instrument.Instrumentation;

@Accessors(fluent = true)
public final class NodeBoot {

    @Getter
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation instrument) {
        instrumentation = instrument;
    }

    public static void main(String[] args) {
        new Node();
    }
}
