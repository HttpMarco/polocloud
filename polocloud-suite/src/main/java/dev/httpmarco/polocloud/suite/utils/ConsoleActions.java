package dev.httpmarco.polocloud.suite.utils;

public class ConsoleActions {

    /**
     * Clear the screen of the logging view
     */
    public static void clearScreen() {
        System.out.println("\033[H\033[2J");
    }
}