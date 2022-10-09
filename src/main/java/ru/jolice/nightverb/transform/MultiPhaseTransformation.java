package ru.jolice.nightverb.transform;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.util.StopWatch;
import ru.jolice.nightverb.file.NoOpPath;
import ru.jolice.nightverb.message.Notifications;
import ru.jolice.nightverb.message.StatusMessage;
import ru.jolice.nightverb.task.OnlineTask;
import ru.jolice.nightverb.transform.context.ProcessingContext;
import ru.jolice.nightverb.transform.phase.ProcessingPhase;

import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class MultiPhaseTransformation implements Transformation {

    private final ThreadLocal<OnlineTask> ctx;
    private final List<ProcessingPhase> phases;
    private final Notifications notifications;

    @Override
    @SneakyThrows
    public Path perform(ProcessingContext ctx) {
        val owner = this.ctx.get();
        Path path = NoOpPath.INSTANCE;
        for (val phase : phases) {
            val watch = new StopWatch();
            watch.start();
            try {
                log.info("Perform phase {}", phase);
                owner.state(phase.state());
                notifications.send(owner.sessionId(), new StatusMessage(phase.state().name().toLowerCase()));
                path = phase.process(path, ctx);
            } finally {
                watch.stop();
                log.info("Phase {} completed in {}", phase, watch.getLastTaskTimeMillis());
            }

        }
        return path;
    }

}
