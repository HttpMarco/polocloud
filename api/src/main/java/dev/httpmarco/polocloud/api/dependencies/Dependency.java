package dev.httpmarco.polocloud.api.dependencies;

import dev.httpmarco.polocloud.runner.RunnerBootstrap;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.jar.JarFile;

@Getter
@Accessors(fluent = true)
public final class Dependency {

    public static final String MAVEN_CENTRAL_REPO = "https://repo1.maven.org/maven2/%s/%s/%s/%s.jar";
    public static final String MAVEN_CENTRAL_SNAPSHOT_REPO = "https://s01.oss.sonatype.org/service/local/repositories/snapshots/content/%s/%s/%s/%s.jar";

    private final String dependencyLink;
    private final File file;

    public static void load(String groupId, String artifactoryId, String version) {
        load(groupId, artifactoryId, version, version, MAVEN_CENTRAL_REPO);
    }

    public static void load(String groupId, String artifactoryId, String version, String subversion, String repository) {
        new Dependency(groupId, artifactoryId, version, subversion, repository);
    }

    @SneakyThrows
    private Dependency(String groupId, String artifactoryId, String version, String subversion, String repository) {
        this.dependencyLink = repository.formatted(groupId.replace(".", "/"), artifactoryId, version, artifactoryId + "-" + subversion);

        var name = groupId + "." + artifactoryId + "." + version;
        var file = RunnerBootstrap.RUNNER.dependencyFolder().resolve(artifactoryId + "-" + version + ".jar");
        this.file = file.toFile();

        if (RunnerBootstrap.INSTRUMENTATION != null) {
            RunnerBootstrap.INSTRUMENTATION.appendToSystemClassLoaderSearch(new JarFile(file()));
        }

        if (!Files.exists(file)) {
            System.err.println("Downloading dependency: " + name + "...");
            if (download()) {
                System.out.println("Successfully downloading dependency " + name);
            } else {
                System.err.println("Cannot download dependency: " + name);
                System.exit(-1);
            }
        }
        RunnerBootstrap.LOADER.addURL(this.file.toURI().toURL());
    }

    //todo with osgan
    private boolean download() {
        try (var inputStream = new URL(dependencyLink).openStream();
             var outputStream = new BufferedOutputStream(new FileOutputStream(file.toString()))) {

            var buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}