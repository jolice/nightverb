package ru.jolice.nightverb.transform.phase;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import ru.jolice.nightverb.effect.AudioEffectFactory;
import ru.jolice.nightverb.file.FilePool;
import ru.jolice.nightverb.task.state.TaskState;
import ru.jolice.nightverb.transform.context.ProcessingContext;

import java.nio.file.Path;

@RequiredArgsConstructor
@Component
public class ApplyEffectPhase implements ProcessingPhase {

    private final FilePool files;
    private final AudioEffectFactory factory;

    @Override
    public Path process(Path currentValue, ProcessingContext ctx) {
        val outputFile = files.allocatePersistent(currentValue);
        val transformation = factory.create(ctx.type());
        transformation.apply(currentValue, outputFile);
        return outputFile;
    }

    @Override
    public TaskState state() {
        return TaskState.PROCESSING_AUDIO;
    }

}
