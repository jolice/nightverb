package ru.jolice.nightverb.transform.phase;

import ru.jolice.nightverb.task.state.TaskState;
import ru.jolice.nightverb.transform.context.ProcessingContext;

import java.nio.file.Path;

public interface ProcessingPhase {

    Path process(Path currentValue, ProcessingContext ctx);

    TaskState state();


}
