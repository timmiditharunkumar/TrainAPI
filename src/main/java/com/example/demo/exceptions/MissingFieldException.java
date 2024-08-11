package com.example.demo.exceptions;

public class MissingFieldException extends RuntimeException {
    public MissingFieldException(String field) {
        super(field +" and cannot be empty.");
    }
}
