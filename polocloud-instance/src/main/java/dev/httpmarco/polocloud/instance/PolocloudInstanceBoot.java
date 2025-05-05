package dev.httpmarco.polocloud.instance;

import java.lang.instrument.Instrumentation;

public final class PolocloudInstanceBoot {

    public static void main(String[] args) {
        new PolocloudInstance(args);
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) {

    }
}
