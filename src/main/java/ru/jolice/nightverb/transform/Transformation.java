package ru.jolice.nightverb.transform;

import ru.jolice.nightverb.transform.context.ProcessingContext;

import java.nio.file.Path;

public interface Transformation {

    Path perform(ProcessingContext ctx);

}
