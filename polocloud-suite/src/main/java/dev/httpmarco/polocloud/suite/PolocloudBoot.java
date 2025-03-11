package dev.httpmarco.polocloud.suite;

import dev.httpmarco.polocloud.suite.terminal.LoggingColors;
import dev.httpmarco.polocloud.suite.utils.ConsoleActions;

import java.lang.instrument.Instrumentation;

public final class PolocloudBoot {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        PolocloudContext.defineContext(instrumentation);
    }

    public static void main(String[] args) {

        // before we start -> clean layout
        ConsoleActions.clearScreen();

        // print header
        ConsoleActions.emptyLine();
        System.out.println(LoggingColors.translate("&b  &fPoloCloud &7- &7Simple minecraft cloud &8(&7v2&8.&70&8.&70&8)"));
        System.out.println(LoggingColors.translate("&b  &7Discord support&8: &fhttps://discord.gg/WGzUcuJax7"));

        // spacer between this important information
        ConsoleActions.emptyLine();

        // register for a random shit shutdown
        PolocloudShutdownHandler.registerShutdownHook();

        // run suite
        new PolocloudSuite();
    }
}