package ru.jolice.nightverb.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message, Exception reason) {
        super(message, reason);
    }

}
