package com.jaelse.acc.lib.exceptions;

public class InvalidIdException extends RuntimeException {

    private String message;

    public InvalidIdException(String message) {
        super(message);
    }
}
