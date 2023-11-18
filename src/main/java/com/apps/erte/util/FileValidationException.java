package com.apps.erte.util;

import java.io.IOException;
public class FileValidationException extends RuntimeException {

    public FileValidationException(String message) {
        super(message);
    }

    public FileValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

