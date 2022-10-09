package ru.jolice.nightverb.transform.phase;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Component;
import ru.jolice.nightverb.file.FilePool;
import ru.jolice.nightverb.limits.LimitException;
import ru.jolice.nightverb.limits.LimitType;
import ru.jolice.nightverb.limits.Limits;
import ru.jolice.nightverb.task.state.TaskState;
import ru.jolice.nightverb.transform.YoutubeVideo;
import ru.jolice.nightverb.transform.context.ProcessingContext;

import java.nio.file.Path;

/**
 * Downloads youtube Video and saves it to MP3 format.
 */
@RequiredArgsConstructor
@Component
public class YtAudioDownloadPhase implements ProcessingPhase {

    private static final String DOWNLOAD_EXT = "mp3";

    private final Limits limits;
    private final FilePool files;

    @Override
    @SneakyThrows
    public Path process(Path currentValue, ProcessingContext ctx) {
        val video = new YoutubeVideo(ctx.videoUrl());
        if (limits.exceedsMaxVideoLength(video.length().getSeconds())) {
            throw new LimitException(LimitType.QUEUE_SIZE, limits.maxVideoLengthSeconds());
        }
        val savedMp3Path = ctx.allocateSessionScoped(video.title() + "." + DOWNLOAD_EXT, files);
        video.saveMP3(savedMp3Path);
        return savedMp3Path;
    }

    @Override
    public TaskState state() {
        return TaskState.PROCESSING_VIDEO;
    }

}
