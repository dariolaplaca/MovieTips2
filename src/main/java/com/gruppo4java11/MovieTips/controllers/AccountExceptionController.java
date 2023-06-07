package com.gruppo4java11.MovieTips.controllers;

import com.gruppo4java11.MovieTips.exception.AccountErrorResponse;
import com.gruppo4java11.MovieTips.exception.AccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AccountExceptionController {
    // Gestore di una singola eccezione, id non trovato
    @ExceptionHandler
    public ResponseEntity<AccountErrorResponse> accountHandlerException(AccountNotFoundException anf){

        AccountErrorResponse accountErrorResponse = new AccountErrorResponse();
        accountErrorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        accountErrorResponse.setMessageError(anf.getMessage());
        accountErrorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(accountErrorResponse,HttpStatus.NOT_FOUND);
    }

    // gestore globale di tutte le eccezioni
    @ExceptionHandler
    public ResponseEntity<AccountErrorResponse> accountHandlerException(Exception exc){

        AccountErrorResponse accountErrorResponse = new AccountErrorResponse();
        accountErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        accountErrorResponse.setMessageError(exc.getMessage());
        accountErrorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(accountErrorResponse,HttpStatus.NOT_FOUND);
    }
}
