package ru.jolice.nightverb.transform.context;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.jolice.nightverb.file.FilePool;
import ru.jolice.nightverb.transform.TransformOptions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public class ProcessingContext {

    @Delegate
    private final TransformOptions options;
    private final List<Path> allocatedTempFiles = new CopyOnWriteArrayList<>();

    public Path allocateSessionScoped(String name, FilePool files) {
        val path = files.allocateTemp(name);
        allocatedTempFiles.add(path);
        return path;
    }

    public void dispose() {
        allocatedTempFiles.forEach(new Consumer<Path>() {
            @Override
            @SneakyThrows
            public void accept(Path path) {
                if (Files.deleteIfExists(path)) {
                    log.info("Deleting {} as the context is being disposed", path);
                }
            }
        });
    }


}
