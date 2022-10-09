package ru.jolice.nightverb.sox;

import lombok.Builder;

@Builder
public class SoxGain implements SoxArgument {

    @Override
    public String asString() {
        return "gain -3 pad 0 3";
    }

}
