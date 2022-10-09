package ru.jolice.nightverb.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Value;

@JsonSerialize(as = HistoryProjection.class)
public interface HistoryProjection {

    @JsonProperty("fileName")
    @Value("#{target.file_name}")
    String getFileName();

    @JsonProperty("fileId")
    @Value("#{target.file_id}")
    String getFileId();

}
