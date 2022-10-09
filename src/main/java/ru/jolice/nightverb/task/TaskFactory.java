package ru.jolice.nightverb.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import ru.jolice.nightverb.entity.TaskMeta;
import ru.jolice.nightverb.limits.LimitException;
import ru.jolice.nightverb.limits.LimitType;
import ru.jolice.nightverb.limits.Limits;
import ru.jolice.nightverb.message.Notifications;
import ru.jolice.nightverb.message.SuccessMessage;
import ru.jolice.nightverb.repository.TaskMetaRepository;
import ru.jolice.nightverb.transform.TransformOptions;
import ru.jolice.nightverb.transform.TransformationFactory;
import ru.jolice.nightverb.transform.context.ProcessingContext;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
public class TaskFactory {

    private final TaskQueue queue;
    private final ThreadLocal<OnlineTask> ctx;
    private final TransformationFactory transformation;
    private final Limits limits;
    private final TaskMetaRepository repository;
    private final TaskRegistry registry;
    private final Notifications notifications;

    @Transactional
    public TaskCreationResult create(TaskOwner owner, TransformOptions options) {

        if (limits.exceedsMaxQueue(queue.activeSize())) {
            throw new LimitException(LimitType.QUEUE_SIZE);
        }
        val meta = new TaskMeta(owner.user(), owner.sessionId());
        repository.save(meta);
        OnlineTask onlineTask = new OnlineTask(transformation.create(), ctx, new ProcessingContext(options), meta);
        registry.put(onlineTask, queue.submit(onlineTask::run)
                .whenComplete(((res, throwable) -> {
                    try {
                        meta.archive();
                        repository.save(meta);
                        notifications.send(owner.sessionId(), new SuccessMessage(
                                meta.file().id().toString()
                        ));
                    } finally {
                        if (!registry.remove(meta.id())) {
                            log.debug("Task was not found during removal");
                        }
                    }
                })));
        return new TaskCreationResult(queue.activeSize(), onlineTask.id());
    }

}
