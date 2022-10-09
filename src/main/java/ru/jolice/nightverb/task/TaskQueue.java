package ru.jolice.nightverb.task;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@RequiredArgsConstructor
public class TaskQueue {

    private final ThreadPoolExecutor executor;

    @Autowired
    public TaskQueue(@Value("${task.queue.max.size:4}") int maxSize) {
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxSize);
    }

    public int activeSize() {
        return executor.getQueue().size();
    }

    public CompletableFuture<?> submit(Runnable task) {
        return CompletableFuture.runAsync(task, executor);
    }

    public void execute(Runnable task) {
        executor.execute(task);
    }

}
