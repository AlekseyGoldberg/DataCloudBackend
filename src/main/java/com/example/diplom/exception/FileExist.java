package com.example.diplom.exception;

public class FileExist extends RuntimeException {
    public FileExist(String message) {
        super(message);
    }
}
