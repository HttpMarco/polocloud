package dev.httpmarco.polocloud.suite;

import dev.httpmarco.polocloud.suite.utils.ConsoleActions;

import java.lang.instrument.Instrumentation;

public final class PolocloudBoot {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        PolocloudContext.defineContext(instrumentation);
    }

    public static void main(String[] args) {
        // before we start -> clean layout
        ConsoleActions.clearScreen();
        // run suite
        new PolocloudSuite();
    }
}