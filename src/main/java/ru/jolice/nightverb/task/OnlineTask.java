package ru.jolice.nightverb.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import ru.jolice.nightverb.entity.TaskMeta;
import ru.jolice.nightverb.exception.BusinessException;
import ru.jolice.nightverb.limits.LimitException;
import ru.jolice.nightverb.task.state.StateAware;
import ru.jolice.nightverb.task.state.TaskState;
import ru.jolice.nightverb.transform.Transformation;
import ru.jolice.nightverb.transform.context.ProcessingContext;

@RequiredArgsConstructor
@Slf4j
public class OnlineTask implements StateAware {

    private final Transformation transformPipeline;
    private final ThreadLocal<OnlineTask> activeTask;
    private final ProcessingContext context;

    @Delegate
    @Getter
    private final TaskMeta meta;

    public void run() {
        try {
            MDC.put("id", String.valueOf(meta.id()));
            activeTask.set(this);
            meta.complete(transformPipeline.perform(this.context));
        } catch (BusinessException e) {
            meta.fail(TaskState.BUSINESS_ERROR, e);
            log.error("Business exception processing task", e);
        } catch (LimitException e) {
            meta.fail(TaskState.LIMIT_HIT, e);
            log.error("Limit hit processing task", e);
        } catch (Throwable e) {
            meta.fail();
            log.error("Other exception processing task", e);
        } finally {
            MDC.clear();
            activeTask.remove();
            context.dispose();
        }
    }

    @Override
    public void update(TaskState newState) {
        this.meta.state(newState);
    }

}
