package dev.httpmarco.polocloud.suite.utils;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class DateFormatter {

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

    public String format(long time) {
        if (time == -1) return "never";

        return formatter.format(new Date(time));
    }
}
