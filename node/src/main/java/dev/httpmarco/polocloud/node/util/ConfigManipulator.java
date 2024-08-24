package dev.httpmarco.polocloud.node.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@RequiredArgsConstructor
public final class ConfigManipulator {

    private final File file;
    private final Map<Predicate<String>, String> replacements = new HashMap<>();

    @Contract("_ -> new")
    public static @NotNull ConfigManipulator of(File file) {
        return new ConfigManipulator(file);
    }

    public void rewrite(Predicate<String> predicate, String line) {
        this.replacements.put(predicate, line);
    }

    @SneakyThrows
    public void write() {
        final var lines = new ArrayList<String>();

        try (var bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        }
        try (final var fileWriter = new FileWriter(file)) {
            for (var line : lines) {

                boolean found = false;
                for (var predicate : replacements.keySet()) {
                    if (!predicate.test(line)) {
                        continue;
                    }
                    found = true;
                    fileWriter.write(replacements.get(predicate) + "\n");
                }

                if (!found) {
                    fileWriter.write(line + "\n");
                }
            }
        }
    }
}
