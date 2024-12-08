package dev.httpmarco.polocloud.node.terminal.utils;

import dev.httpmarco.polocloud.node.terminal.JLineTerminalColor;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class TerminalColorReplacer {

    private static final String COLOR_INDEX = "&";

    public String replaceColorCodes(String message) {
        for (var color : JLineTerminalColor.values()) {
            message = message.replace((COLOR_INDEX + color.key()), color.ansiCode());
        }
        return message;
    }
}
