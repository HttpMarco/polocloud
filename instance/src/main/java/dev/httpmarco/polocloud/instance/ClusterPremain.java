package dev.httpmarco.polocloud.instance;

import java.lang.instrument.Instrumentation;

public final class ClusterPremain {

    public static Instrumentation INSTRUMENTATION;

    public static void premain(String args, Instrumentation instrumentation) {
        INSTRUMENTATION = instrumentation;
    }
}
