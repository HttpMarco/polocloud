package dev.httpmarco.polocloud.suite;

import java.lang.instrument.Instrumentation;

public final class PolocloudBoot {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        PolocloudContext.defineContext(instrumentation);
    }

    public static void main(String[] args) {
        new PolocloudSuite();
    }
}