package ru.jolice.nightverb.sox;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "audio.transform.sox.reverb")
public class SoxProperties {

    private int damping = 100;
    private int roomScale = 100;
    private int stereoDepth = 100;
    private int preDelay = 100;
    private int wetGain = 500;


}
