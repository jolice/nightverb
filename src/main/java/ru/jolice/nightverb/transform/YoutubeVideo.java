package ru.jolice.nightverb.transform;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.time.Duration;

import static java.lang.Long.parseLong;
import static java.time.Duration.*;

@RequiredArgsConstructor
@Slf4j
public class YoutubeVideo {

    private static final String YT_DLP_PATH_VAR = "YT_DLP_PATH";

    static {
        var dlpPath = System.getenv(YT_DLP_PATH_VAR);
        if (dlpPath == null) {
            dlpPath = System.getProperty(YT_DLP_PATH_VAR, "/usr/local/bin/yt-dlp");

        }
        YoutubeDL.setExecutablePath(dlpPath);
        try {
            YoutubeDL.getVersion();
        } catch (YoutubeDLException e) {
            throw new IllegalStateException("Failed to load yt-dlp: " + e);
        }
    }

    private final String url;
    private volatile CachedMetadata metadata;

    @RequiredArgsConstructor
    @Getter
    static class CachedMetadata {

        private final YoutubeDuration duration;
        private final String title;

    }

    @SneakyThrows
    public void saveMP3(Path outputPath) {
        YoutubeDLRequest request = new YoutubeDLRequest(url);
        request.setOption("extract-audio");        // --ignore-errors
        request.setOption("output", outputPath.toString());
        request.setOption("audio-format", "mp3");    // --output "%(id)s"
        request.setOption("retries", 10);        // --retries 10
        val execute = YoutubeDL.execute(request);
        log.info("Executed: {} in {} ms, code: {}", execute.getCommand(), execute.getElapsedTime(),
                execute.getExitCode()
        );
    }

    private CachedMetadata metadata() {
        if (metadata == null) {
            synchronized (this) {
                if (metadata == null) {
                    metadata = fetchMetadata();
                    return this.metadata;
                }
            }
        }
        return this.metadata;
    }

    @SneakyThrows
    private CachedMetadata fetchMetadata() {
        YoutubeDLRequest request = new YoutubeDLRequest(url);
        request.setOption("get-duration");        // --ignore-errors
        request.setOption("get-title");
        request.setOption("retries", 10);        // --retries 10
        val result = YoutubeDL.execute(request).getOut().split("\n");
        return new CachedMetadata(
                new YoutubeDuration(result[1]),
                result[0]
        );
    }

    public String title() {
        return metadata().title().replace(" ", "");
    }

    public Duration length() {
        return metadata().duration.get();
    }

    @RequiredArgsConstructor
    static class YoutubeDuration {

        private final String sourceLine;

        public Duration get() {
            val split = sourceLine.split(":");
            if (split.length == 2) {
                return ofMinutes(parseLong(split[0]))
                        .plus(ofSeconds(parseLong(split[1])));
            } else if (split.length == 3) {
                return ofHours(parseLong(split[0]))
                        .plus(ofMinutes(parseLong(split[1])))
                        .plus(ofSeconds(parseLong(split[2])));
            } else {
                throw new IllegalArgumentException("Unsupported: " + sourceLine);
            }
        }

    }


}
