package dev.httpmarco.polocloud.suite.commands.impl;

import com.sun.management.OperatingSystemMXBean;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.commands.Command;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.time.Duration;

@Log4j2
public final class InfoCommand extends Command {

    private static final DecimalFormat FORMAT = new DecimalFormat("#.##");

    public InfoCommand() {
        super("info", "Show all information about your running cluster.");

        var translation = PolocloudSuite.instance().translation();

        defaultExecution(it -> {
            // todo
            log.info(PolocloudSuite.instance().translation().get("suite.command.info.header", 1));

            double usedMemory = usedMemory();
            double maxMemory = maxMemory();
            log.info(translation.get("suite.command.info.memory.current", FORMAT.format(usedMemory), Math.round((100 / maxMemory) * usedMemory)));
            log.info(translation.get("suite.command.info.memory.max",  FORMAT.format(maxMemory)));
            log.info(translation.get("suite.command.info.cpu", cpuUsage()));
            log.info(translation.get("suite.command.info.uptime", formatDuration(System.currentTimeMillis() - Long.parseLong(System.getProperty("polocloud.boot.time")))));
            log.info(translation.get("suite.command.info.java", System.getProperty("java.version")));
            log.info(translation.get("suite.command.info.clusterType", PolocloudSuite.instance().cluster().name()));
            log.info(translation.get("suite.command.info.maxQueue", PolocloudSuite.instance().config().local().maxQueueProcesses()));
            log.info(translation.get("suite.command.info.idleShutdown", PolocloudSuite.instance().config().local().processTerminationIdleSeconds()));
        });
    }

    public double usedMemory() {
        var runtime = Runtime.getRuntime();
        var usedBytes = runtime.totalMemory() - runtime.freeMemory();

        return calculateMemory(usedBytes);
    }

    public double maxMemory() {
        var runtime = Runtime.getRuntime();
        var maxBytes = runtime.maxMemory();

        return calculateMemory(maxBytes);
    }

    public double cpuUsage() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double load = osBean.getCpuLoad();
        if (load < 0) return -1;
        return Math.round(load * 10000.0) / 100.0;
    }

    private double calculateMemory(long bytes) {
        return bytes / 1024.0 / 1024.0;
    }

    public static @NotNull String formatDuration(long millis) {
        Duration duration = Duration.ofMillis(millis);

        long days = duration.toDays();
        duration = duration.minusDays(days);

        long hours = duration.toHours();
        duration = duration.minusHours(hours);

        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);

        long seconds = duration.getSeconds();
        StringBuilder sb = new StringBuilder();

        if (days > 0) sb.append(days).append("d ");
        if (hours > 0 || days > 0) sb.append(hours).append("h ");
        if (minutes > 0 || hours > 0 || days > 0) sb.append(minutes).append("m ");
        if (seconds > 0 || minutes > 0 || hours > 0 || days > 0) sb.append(seconds).append("s ");

        return sb.toString();
    }
}
