package dev.httpmarco.polocloud.node.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ConsoleActions {

    public void cleanLines() {
        System.out.println("\033[H\033[2J");
    }
}
