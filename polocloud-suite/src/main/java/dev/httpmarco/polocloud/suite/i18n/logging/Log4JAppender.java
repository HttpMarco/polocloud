package dev.httpmarco.polocloud.suite.i18n.logging;

import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.terminal.LoggingColors;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Plugin(name = "Log4JAppender", category = Core.CATEGORY_NAME, elementType = "appender", printObject = true)
public final class Log4JAppender extends AbstractAppender {

    private final SimpleDateFormat logFormat = new SimpleDateFormat("HH:mm:ss");

    private Log4JAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    @Contract(value = " -> new", pure = true)
    @PluginBuilderFactory
    public static @NotNull Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends AbstractAppender.Builder<Builder> implements org.apache.logging.log4j.core.util.Builder<Log4JAppender> {
        @Override
        public Log4JAppender build() {
            return new Log4JAppender(getName(), getFilter(), getLayout(), isIgnoreExceptions(), getPropertyArray());
        }
    }

    @Override
    public void append(@NotNull LogEvent event) {
        var message = LoggingColors.translate(logFormat.format(new Date()) + " &8| " + detectState(event.getLevel()) + "&8 » &7" + event.getMessage().getFormattedMessage());
        var terminal = PolocloudSuite.instance().terminal();

        if (terminal != null) {
            terminal.print(message);
            return;
        }
        // if no terminal is present, print to console
        System.out.println(message);
    }

    private String detectState(Level level) {
        if (level.equals(Level.INFO)) {
            return LoggingColors.GRAY + "INFO";
        } else if (level.equals(Level.WARN)) {
            return LoggingColors.YELLOW + "WARN";
        } else if (level.equals(Level.ERROR)) {
            return LoggingColors.RED + "ERROR";
        } else if (level.equals(Level.FATAL)) {
            return LoggingColors.RED + "FATAL";
        } else if (level.equals(Level.DEBUG)) {
            return LoggingColors.WHITE + "DEBUG";
        }
        return LoggingColors.GRAY + "UNKNOWN";
    }
}
