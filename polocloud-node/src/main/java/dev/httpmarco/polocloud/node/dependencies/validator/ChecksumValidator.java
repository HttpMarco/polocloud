package dev.httpmarco.polocloud.node.dependencies.validator;

import dev.httpmarco.polocloud.api.Validate;
import dev.httpmarco.polocloud.node.dependencies.Dependency;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

/**
 * Validates the checksum of a dependency
 */
public final class ChecksumValidator implements Validate<Dependency, Path> {

    @Override
    public boolean valid(@NotNull Dependency dependency, Path path) {
        return dependency.checksum() != null && dependency.checksum().equals(dependencyChecksum(path));
    }

    @SneakyThrows
    private @NotNull String dependencyChecksum(Path path) {
        var digest = MessageDigest.getInstance("SHA-1");
        var fileBytes = Files.readAllBytes(path);
        var hashBytes = digest.digest(fileBytes);
        var hexString = new StringBuilder();

        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
