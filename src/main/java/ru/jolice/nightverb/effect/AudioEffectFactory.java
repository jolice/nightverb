package ru.jolice.nightverb.effect;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.jolice.nightverb.sox.SoxBinary;

@Component
@RequiredArgsConstructor
public class AudioEffectFactory {

    private final SoxBinary binary;

    public AudioEffect create(TransformationType type) {
        if (type == TransformationType.NIGHTCORE) {
            return new NightCoreEffect();
        } else if (type == TransformationType.REVERB) {
            return new ReverbEffect(binary);
        } else {
            throw new IllegalArgumentException("Not supported yet: " + type);
        }
    }

}
