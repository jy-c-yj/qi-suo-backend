package com.cyj.exception;

public class ProductStockIsNotException extends RuntimeException{
    public ProductStockIsNotException(String message) {
        super(message);
    }
}
