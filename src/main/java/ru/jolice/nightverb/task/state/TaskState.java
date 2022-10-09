package ru.jolice.nightverb.task.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TaskState {

    /**
     * Task is not started yet
     */
    PENDING_QUEUE(StateCategory.PROGRESS),

    /**
     * Reading Video Metadata
     */
    READ_VIDEO_METADATA(StateCategory.PROGRESS),

    /**
     * Downloading youtube video and converting to MP3
     */
    PROCESSING_VIDEO(StateCategory.PROGRESS),

    /**
     * Processing
     */
    PROCESSING_AUDIO(StateCategory.PROGRESS),

    /**
     * Done
     */
    COMPLETED(StateCategory.SUCCESS),

    /**
     * Error
     */
    OTHER_ERROR(StateCategory.ERROR),

    /**
     * Error
     */
    BUSINESS_ERROR(StateCategory.ERROR),


    /**
     * Task has been rejected due a timeout or another reason
     */
    REJECTED(StateCategory.ERROR),


    /**
     * Task has been rejected due a timeout or another reason
     */
    LIMIT_HIT(StateCategory.ERROR);

    @Getter
    private final StateCategory category;

    public static TaskState initial() {
        return TaskState.PENDING_QUEUE;
    }


}
