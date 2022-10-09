package ru.jolice.nightverb.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class TaskRegistry {

    private final Map<Long, Entry> tasks = new ConcurrentHashMap<>();

    public void put(OnlineTask task, CompletableFuture<?> action) {
        this.tasks.put(task.id(), new Entry(action, task));
    }

    public OnlineTask get(long key) {
        val entry = tasks.get(key);
        if (entry == null) {
            return null;
        }
        return entry.task;
    }

    public boolean remove(Long id) {
        val prevTask = tasks.get(id);
        if (prevTask == null) {
            return false;
        }
        if (!prevTask.cancel()) {
            log.warn("Task {} has been already cancelled", id);
        }
        return true;
    }

    public List<OnlineTask> expire(long timeout) {
        val iterator = tasks.entrySet().iterator();
        List<OnlineTask> result = new ArrayList<>();
        while (iterator.hasNext()) {
            val next = iterator.next();
            val entry = next.getValue();
            if (Duration.between(entry.creationTime, Instant.now()).getSeconds() >= timeout) {
                log.debug("Cancelling task {} by timeout", next.getKey());
                entry.cancel();
                iterator.remove();
                result.add(entry.task);
            }
        }
        return result;
    }

    @RequiredArgsConstructor
    private static class Entry {

        private final Instant creationTime = Instant.now();
        private final CompletableFuture<?> execution;
        private final OnlineTask task;
        private volatile boolean cancelled;

        public boolean cancel() {
            synchronized (this) {
                if (!cancelled) {
                    this.execution.cancel(true);
                    this.cancelled = true;
                    return true;
                }
                return false;
            }
        }

    }

}
