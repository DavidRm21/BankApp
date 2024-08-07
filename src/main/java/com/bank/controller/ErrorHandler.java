package com.bank.controller;

import com.itextpdf.text.DocumentException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    public Map<String, Object> handlerException(Exception e){
        Map<String, Object> response = new HashMap<>();
        response.put("code", 99);
        response.put("message", e.getMessage());
        return response;
    }

    @ExceptionHandler(FileNotFoundException.class)
    public Map<String, Object> handlerFileNotFoundException(FileNotFoundException e){
        Map<String, Object> response = new HashMap<>();
        response.put("code", 2);
        response.put("message", "Archivo no encontrado.");
        return response;
    }

    @ExceptionHandler(DocumentException.class)
    public Map<String, Object> handlerDocumentException(DocumentException e){
        Map<String, Object> response = new HashMap<>();
        response.put("code", 3);
        response.put("message", "Error en el documento");
        return response;
    }

}
