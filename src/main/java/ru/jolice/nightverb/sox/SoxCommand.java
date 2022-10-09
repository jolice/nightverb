package ru.jolice.nightverb.sox;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class SoxCommand {

    private final Path in;
    private final Path out;
    private final List<SoxArgument> arguments = new ArrayList<>();

    public SoxCommand arg(SoxArgument argument) {
        this.arguments.add(argument);
        return this;
    }

    @SneakyThrows
    public void execute(SoxBinary binary) {
        val builder = new StringBuilder();
        builder.append(String.format("%s %s %s ", binary.path(), in, out));
        for (SoxArgument argument : arguments) {
            builder.append(argument.asString()).append(" ");
        }
        val cmd = builder.toString();
        log.info("Executing SOX: {}", cmd);
        val process = Runtime.getRuntime().exec(cmd);
        val errOutput = IOUtils.toString(process.getErrorStream(), StandardCharsets.UTF_8);
        val stdOutput = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
        if (!errOutput.isEmpty()) {
            log.info("SOX err: {}", errOutput);
        }
        if (!stdOutput.isEmpty()) {
            log.debug("SOX out: {}", stdOutput);
        }
        val exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("SOX exit code: " + exitCode);
        }
    }


}
