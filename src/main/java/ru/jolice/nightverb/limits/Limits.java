package ru.jolice.nightverb.limits;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Limits {

    private volatile int maxQueue = 10;
    private volatile int maxVideoLengthSeconds = 60 * 10;

    public boolean exceedsMaxQueue(int value) {
        return value >= maxQueue;
    }

    public boolean exceedsMaxVideoLength(long value) {
        return value >= maxVideoLengthSeconds;
    }

}
