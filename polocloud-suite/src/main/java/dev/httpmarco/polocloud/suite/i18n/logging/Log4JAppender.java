package dev.httpmarco.polocloud.suite.i18n.logging;

import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Plugin(name = "Log4JAppender", category = Core.CATEGORY_NAME, elementType = "appender", printObject = true)
public final class Log4JAppender extends AbstractAppender {

    private final SimpleDateFormat logFormat = new SimpleDateFormat("HH:mm:ss");

    private Log4JAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends AbstractAppender.Builder<Builder> implements org.apache.logging.log4j.core.util.Builder<Log4JAppender> {
        @Override
        public Log4JAppender build() {
            return new Log4JAppender(getName(), getFilter(), getLayout(), isIgnoreExceptions(), getPropertyArray());
        }
    }

    @Override
    public void append(LogEvent event) {
        System.out.println(LoggingColors.translate(logFormat.format(new Date()) + " &8| &7" + event.getMessage().getFormattedMessage()));
    }
}
