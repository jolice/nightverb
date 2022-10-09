package ru.jolice.nightverb.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.jolice.nightverb.entity.User;

@RequiredArgsConstructor
public class TaskOwner {

    @Getter
    private final User user;

    @Getter
    private final String sessionId;

}
