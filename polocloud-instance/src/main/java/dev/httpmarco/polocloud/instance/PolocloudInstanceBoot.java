package dev.httpmarco.polocloud.instance;

import java.lang.instrument.Instrumentation;

public final class PolocloudInstanceBoot {

    public static void main(String[] args) {
        new PolocloudInstance();
    }

    public static void instrumentation(Instrumentation instrumentation) {

    }
}
