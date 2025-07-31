package dev.httpmarco.polocloud.updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class UpdaterRuntime {

    public static void main(String[] args) throws InterruptedException {

        Thread.sleep(5000);

        String downloadUrl = "https://github.com/HttpMarco/polocloud/releases/download/v3.0.0-pre-3/polocloud-launcher.jar";
        File targetFile = new File("../../polocloud-launcher.jar");

        downloadJarFromGitHub(downloadUrl, targetFile);
        startLauncherAndExit();
    }

    public static void downloadJarFromGitHub(String downloadUrl, File targetFile) {
        System.out.println("⬇️  Starte Download von: " + downloadUrl);

        try (InputStream input = new URL(downloadUrl).openStream();
             ReadableByteChannel channel = Channels.newChannel(input);
             FileOutputStream output = new FileOutputStream(targetFile)) {

            output.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
            System.out.println("✅ Download abgeschlossen: " + targetFile.getAbsolutePath());

        } catch (Exception e) {
            System.err.println("❌ Fehler beim Download: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void startLauncherAndExit() {
        try {
            ProcessBuilder builder = new ProcessBuilder("java", "-jar", "polocloud-launcher.jar");
            builder.directory(new File("../../"));
            builder.start();

            System.exit(0);
        } catch (Exception e) {
            System.err.println("❌ Fehler beim Starten des Launchers: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
