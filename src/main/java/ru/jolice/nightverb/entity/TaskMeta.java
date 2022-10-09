package ru.jolice.nightverb.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jolice.nightverb.task.state.TaskState;

import javax.persistence.*;
import java.nio.file.Path;

@Table(name = "as_tasks")
@Data
@Entity
@NoArgsConstructor
public class TaskMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private TaskState state = TaskState.initial();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private SavedFile file = new SavedFile(this);

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "limit_message")
    private String limitMessage;

    @Column(name = "archived")
    private boolean archived;

    @Column(name = "ws_session_id")
    private String sessionId;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public TaskMeta(User user, String sessionId) {
        this.sessionId = sessionId;
        this.user = user;
    }

    public void fail(TaskState state, Throwable errorMessage) {
        this.state = state;
        this.errorMessage = errorMessage.getMessage();
    }

    public void fail() {
        this.state = TaskState.OTHER_ERROR;
    }

    public void archive() {
        this.archived = true;
    }

    public void complete(Path outputFile) {
        this.state = TaskState.COMPLETED;
        this.file.path(outputFile);
    }

}
