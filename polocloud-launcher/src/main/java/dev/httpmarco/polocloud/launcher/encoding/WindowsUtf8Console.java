package dev.httpmarco.polocloud.launcher.encoding;

import java.io.IOException;

public final class WindowsUtf8Console {

    public static void setUtf8Encoding() {
        if (isWindows()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "chcp 65001");
                pb.inheritIO();
                Process process = pb.start();
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }
        System.setProperty("file.encoding", "UTF-8");
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
