package ru.jolice.nightverb.effect;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class NightCoreEffect implements AudioEffect {

    @Override
    @SneakyThrows
    public void apply(Path input, Path outFile) {
        FFmpeg ffmpeg = new FFmpeg();
        FFprobe ffprobe = new FFprobe();

        final FFmpegProbeResult in = ffprobe.probe(input.toString());
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        FFmpegBuilder builder = new FFmpegBuilder().setInput(input.toString())
                .setAudioFilter("aresample=48000,asetrate=48000*1.25")
                .addOutput(outFile.toString())
                .done();

        FFmpegJob job = executor.createJob(builder, new ProgressListener() {

            final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);

            @Override
            public void progress(Progress progress) {
                double percentage = progress.out_time_ns / duration_ns;
                log.debug(String.format(
                        "[%.0f%%] status:%s frame:%d time:%s ms fps:%.0f speed:%.2fx",
                        percentage * 100,
                        progress.status,
                        progress.frame,
                        FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
                        progress.fps.doubleValue(),
                        progress.speed
                ));
            }
        });
        job.run();
    }

}
