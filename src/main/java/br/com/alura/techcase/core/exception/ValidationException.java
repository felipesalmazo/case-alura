package br.com.alura.techcase.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ValidationException extends Exception{
    public ValidationException(String message) {
        super(message);
    }
}
