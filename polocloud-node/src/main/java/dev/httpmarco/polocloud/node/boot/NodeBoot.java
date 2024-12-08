package dev.httpmarco.polocloud.node.boot;

import dev.httpmarco.polocloud.node.Node;

import java.lang.instrument.Instrumentation;

public final class NodeBoot {

    public static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation instrument) {
        instrumentation = instrument;
    }

    public static void main(String[] args) {
        new Node();
    }
}
