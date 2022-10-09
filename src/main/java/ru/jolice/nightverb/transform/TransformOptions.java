package ru.jolice.nightverb.transform;

import lombok.Value;
import ru.jolice.nightverb.effect.TransformationType;

@Value
public class TransformOptions {

    TransformationType type;
    String videoUrl;

}
