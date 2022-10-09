package ru.jolice.nightverb.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import ru.jolice.nightverb.task.state.StateCategory;
import ru.jolice.nightverb.task.state.TaskState;

@Value
public class TaskStatusDto implements Response {

    TaskState state;
    String id;

    @JsonProperty("state")
    public TaskState state() {
        return state;
    }

    @JsonProperty("category")
    public StateCategory category() {
        return state.category();
    }

}
