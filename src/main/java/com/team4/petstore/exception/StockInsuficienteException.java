package com.team4.petstore.exception;

public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(String message) {
        super(message);
    }
}
