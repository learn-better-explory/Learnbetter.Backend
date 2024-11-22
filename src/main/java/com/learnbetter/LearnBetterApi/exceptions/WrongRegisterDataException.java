package com.learnbetter.LearnBetterApi.exceptions;

public class WrongRegisterDataException extends RuntimeException {
    public WrongRegisterDataException(String message) {
        super(message);
    }
}
