package ru.jolice.nightverb.file;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Proxy;
import java.nio.file.Path;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoOpPath {

    public static final Path INSTANCE;

    static {
        INSTANCE = (Path) Proxy.newProxyInstance(NoOpPath.class.getClassLoader(), new Class[]{Path.class}, (proxy, method, args) -> {
            throw new UnsupportedOperationException("This is no-op path, meaning there's no value present");
        });
    }

}
