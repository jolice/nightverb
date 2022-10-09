package ru.jolice.nightverb.transform;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.jolice.nightverb.message.Notifications;
import ru.jolice.nightverb.task.OnlineTask;
import ru.jolice.nightverb.transform.phase.ApplyEffectPhase;
import ru.jolice.nightverb.transform.phase.YtAudioDownloadPhase;

import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class TransformationFactory {

    private final ApplyEffectPhase applyEffectPhase;
    private final YtAudioDownloadPhase audioDownloadPhase;
    private final ThreadLocal<OnlineTask> ctx;
    private final Notifications notifications;

    public Transformation create() {
        return new MultiPhaseTransformation(
                ctx,
                Arrays.asList(audioDownloadPhase, applyEffectPhase),
                notifications
        );
    }

}
