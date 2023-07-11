package com.dhorbach.codingchallenge.exceptions;

/**
 * Exception indicating that a resource was not found.
 */
public class NotFoundException extends RuntimeException{

    public NotFoundException(final String message) {
        super(message);
    }
}
