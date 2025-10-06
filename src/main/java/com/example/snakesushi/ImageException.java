package com.example.snakesushi;

public class ImageException extends RuntimeException {
    private final int status;

    public ImageException(String message, int status) {
        super(message);
        this.status = status;
    }
}
