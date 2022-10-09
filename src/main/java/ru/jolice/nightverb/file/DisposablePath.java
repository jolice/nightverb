package ru.jolice.nightverb.file;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
@Slf4j
public class DisposablePath implements Path, AutoCloseable {

    @Delegate
    private final Path path;

    @Override
    public void close() throws Exception {
        if (Files.deleteIfExists(this.path)) {
            log.info("Deleted {}", this.path);
        }
    }

}
