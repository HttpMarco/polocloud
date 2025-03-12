package dev.httpmarco.polocloud.suite.terminal;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.fusesource.jansi.Ansi;

@Getter
@Accessors(fluent = true)
public enum LoggingColors {

    BLACK("&0", Ansi.Color.BLACK),
    DARK_BLUE("&1", Ansi.Color.BLUE),
    DARK_GREEN("&2", Ansi.Color.GREEN),
    DARK_AQUA("&3", Ansi.Color.CYAN),
    DARK_RED("&4", Ansi.Color.RED),
    DARK_PURPLE("&5", Ansi.Color.MAGENTA),
    GOLD("&6", Ansi.Color.YELLOW),
    GRAY("&7", Ansi.Color.WHITE),
    DARK_GRAY("&8", Ansi.Color.BLACK, true),
    BLUE("&9", Ansi.Color.BLUE, true),
    GREEN("&a", Ansi.Color.GREEN, true),
    AQUA("&b", Ansi.Color.CYAN, true),
    RED("&c", Ansi.Color.RED, true),
    LIGHT_PURPLE("&d", Ansi.Color.MAGENTA, true),
    YELLOW("&e", Ansi.Color.YELLOW, true),
    WHITE("&f", Ansi.Color.WHITE, true),
    RESET("&r", null);

    private final String code;
    private final Ansi.Color color;
    private final boolean bright;

    LoggingColors(String code, Ansi.Color color, boolean bright) {
        this.code = code;
        this.color = color;
        this.bright = bright;
    }

    LoggingColors(String code, Ansi.Color color) {
        this(code, color, false);
    }

    public String ansi() {
        if (color == null) {
            return Ansi.ansi().reset().toString();
        }
        return bright ? Ansi.ansi().fgBright(color).toString() : Ansi.ansi().fg(color).toString();
    }

    public static String translate(String message) {
        for (LoggingColors color : values()) {
            message = message.replace(color.code(), color.ansi());
        }
        return message + LoggingColors.RESET.ansi();
    }

    @Override
    public String toString() {
        return this.ansi();
    }
}