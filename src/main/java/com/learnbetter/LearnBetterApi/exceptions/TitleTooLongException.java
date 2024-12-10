package com.learnbetter.LearnBetterApi.exceptions;

public class TitleTooLongException extends RuntimeException {
    public TitleTooLongException(String message) {
        super(message);
    }
}
