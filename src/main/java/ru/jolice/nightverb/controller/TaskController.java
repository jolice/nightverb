package ru.jolice.nightverb.controller;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jolice.nightverb.dto.in.CreateTaskDto;
import ru.jolice.nightverb.dto.out.TaskStatusDto;
import ru.jolice.nightverb.entity.TaskMeta;
import ru.jolice.nightverb.repository.HistoryProjection;
import ru.jolice.nightverb.repository.TaskMetaRepository;
import ru.jolice.nightverb.repository.UserRepository;
import ru.jolice.nightverb.task.TaskCreationResult;
import ru.jolice.nightverb.task.TaskFactory;
import ru.jolice.nightverb.task.TaskOwner;
import ru.jolice.nightverb.task.TaskRegistry;
import ru.jolice.nightverb.transform.TransformOptions;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskFactory factory;
    private final TaskRegistry registry;
    private final TaskMetaRepository repository;
    private final UserRepository userRepository;

    @GetMapping("/task/history")
    public List<HistoryProjection> getRecentHistory(@RequestHeader("SessionUserId") Long userId) {
        return repository.getRecentHistory(userId);
    }

    @PostMapping("/task/create")
    public ResponseEntity<TaskCreationResult> factory(@Valid @RequestBody CreateTaskDto body,
                                                      @RequestHeader("SessionUserId") Long userId) {
        val user =
                userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        return ResponseEntity.ok(
                factory.create(new TaskOwner(user, body.sessionId()),
                        new TransformOptions(body.type(), body.youtubeUrl()))
        );
    }

    @GetMapping("/task/status/{id}")
    public ResponseEntity<TaskStatusDto> get(@PathVariable("id") long key) {
        TaskMeta task = registry.get(key).meta();
        if (task == null) {
            val dbRef = repository.findById(key);
            if (!dbRef.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            task = dbRef.get();
        }
        return ResponseEntity.ok(new TaskStatusDto(task.state(), task.file().id()));
    }


}
