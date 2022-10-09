package ru.jolice.nightverb.sox;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SoxBinary {


    @Getter
    private final String path;

    public SoxBinary(@Value("${SOX_PATH:/bin/sox}") String path) {
        try {
            if (path == null) {
                throw new IllegalArgumentException("Sox path is null");
            }
            Runtime.getRuntime().exec(path);
        } catch (IOException e) {
            throw new IllegalStateException("Error trying to execute sox", e);
        }
        this.path = path;
    }


}
