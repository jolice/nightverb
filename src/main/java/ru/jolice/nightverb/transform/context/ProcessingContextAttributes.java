package ru.jolice.nightverb.transform.context;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class ProcessingContextAttributes {


    @EqualsAndHashCode
    @ToString
    @RequiredArgsConstructor
    private static final class Attr implements ProcessingContextAttribute {

        private final String key;

    }

}
