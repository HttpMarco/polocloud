package dev.httpmarco.polocloud.base.terminal;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.fusesource.jansi.Ansi;

@Getter
public enum CloudTerminalColor {

    DEFAULT(209, 209, 209),
    DARK_GRAY(69, 69, 69),
    WHITE(255, 255, 255),
    INFO(125, 246, 255),
    WARNING(232, 164, 77),
    ERROR( 247, 74, 74),
    PROMPT(130, 234, 255),
    SUCCESS(157, 250, 178);

    public static final CloudTerminalColor[] colors = values();

    @Accessors(fluent = true)
    private final String ansiCode;

    CloudTerminalColor(int red, int green, int blue) {
        this.ansiCode = Ansi.ansi().a(Ansi.Attribute.RESET).fgRgb(red, green, blue).toString();
    }
}
