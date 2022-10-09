package ru.jolice.nightverb.message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SuccessMessage {

    private final String status = "success";
    private final String fileUid;


}
