package br.com.alura.techcase.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class NotFoundException extends Exception{
    public NotFoundException(String message) {
        super(message);
    }
}
