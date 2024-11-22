package com.learnbetter.LearnBetterApi.exceptions;

public class PasswordDoesntMatchException extends RuntimeException {
    public PasswordDoesntMatchException(String message) {
        super(message);
    }
}
