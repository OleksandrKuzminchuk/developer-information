package com.sasha.jdbccrud.exception;

public class NotValidException extends RuntimeException {
    public NotValidException(String message) {
        super(message);
    }
}
