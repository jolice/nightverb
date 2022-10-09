package ru.jolice.nightverb.dto.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.jolice.nightverb.effect.TransformationType;

import javax.annotation.MatchesPattern;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class CreateTaskDto {

    @NotNull
    private TransformationType type;

    @MatchesPattern("^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube(-nocookie)?\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$")
    private String youtubeUrl;

    @NotBlank
    private String sessionId;


}
