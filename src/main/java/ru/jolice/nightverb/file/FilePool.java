package ru.jolice.nightverb.file;

import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FilePool {

    private static final Path BASE_PATH = Paths.get("work");
    private static final Path PERSISTENT = BASE_PATH.resolve("persistent");
    private static final Path TEMP = BASE_PATH.resolve("tmp");

    static {
        try {
            Files.createDirectories(TEMP);
            Files.createDirectories(PERSISTENT);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to create work directories", e);
        }
    }

    @SneakyThrows
    public Path allocateTemp(String name) {
        val tmpDir = Files.createTempDirectory(TEMP, "");
        return tmpDir.resolve(name);
    }

    @SneakyThrows
    public Path allocatePersistent(Path name) {
        val tmpDir = Files.createTempDirectory(PERSISTENT, "");
        return tmpDir.resolve(name.getFileName().toString());
    }

}
