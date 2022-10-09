package ru.jolice.nightverb.task;

import lombok.Value;

@Value
public class TaskCreationResult {

    int queuePosition;
    long taskId;

}
