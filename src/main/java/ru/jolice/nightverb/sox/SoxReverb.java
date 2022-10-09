package ru.jolice.nightverb.sox;

import lombok.Builder;
import lombok.NonNull;

@Builder
@NonNull
public class SoxReverb implements SoxArgument {

    public static SoxReverb DEFAULT = SoxReverb.builder()
            .reverberance(80)
            .damping(50)
            .roomScale(100)
            .stereoDepth(100)
            .preDelay(0)
            .wetGain(0)
            .build();

    private final int reverberance;
    private final int damping;
    private final int roomScale;
    private final int stereoDepth;
    private final int preDelay;
    private final int wetGain;

    /*
     [reverberance (50%)
     [HF-damping (50%)
     [room-scale (100%)
     [stereo-depth (100%)
     [pre-delay (0ms)
     [wet-gain (0dB)]]
    */
    @Override
    public String asString() {
        return String.format("reverb %d %d %d %d %d %d", reverberance, damping, roomScale, stereoDepth, preDelay, wetGain);
    }

}
