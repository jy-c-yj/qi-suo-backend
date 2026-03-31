package com.cyj.exception;

public class ProductIsNotExistException extends RuntimeException{
    public ProductIsNotExistException(String message) {
        super(message);
    }
}
