package ru.jolice.nightverb.effect;

import java.nio.file.Path;

public interface AudioEffect {

    void apply(Path in, Path out);

}
