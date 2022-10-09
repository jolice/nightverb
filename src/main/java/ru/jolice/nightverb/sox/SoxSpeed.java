package ru.jolice.nightverb.sox;

import lombok.Builder;

@Builder
public class SoxSpeed implements SoxArgument {

    public static final SoxSpeed DEFAULT = SoxSpeed.builder()
            .speed(0.87F)
            .build();

    private final float speed;

    @Override
    public String asString() {
        return String.format("speed %f", speed);
    }

}
