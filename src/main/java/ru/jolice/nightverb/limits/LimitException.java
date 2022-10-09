package ru.jolice.nightverb.limits;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LimitException extends RuntimeException {

    private final LimitType type;
    private final Object limitValue;

    public LimitException(LimitType type) {
        this(null, type);
    }


}
