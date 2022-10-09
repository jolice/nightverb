package ru.jolice.nightverb.effect;

import lombok.RequiredArgsConstructor;
import ru.jolice.nightverb.sox.*;

import java.nio.file.Path;

@RequiredArgsConstructor
public class ReverbEffect implements AudioEffect {

    private final SoxBinary binary;

    @Override
    public void apply(Path in, Path out) {
        new SoxCommand(in, out)
                .arg(SoxGain.builder().build())
                .arg(SoxReverb.DEFAULT)
                .arg(SoxSpeed.DEFAULT)
                .execute(binary);
    }

}
