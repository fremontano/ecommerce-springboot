package com.ecommerce.sb_ecom.exceptions;


import com.ecommerce.sb_ecom.payload.APIResponse;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

//Manejo global de excepciones

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    //Devuelve un Map<String, String>, donde.
    //Clave: nombre del campo con error.
    //Valor: mensaje de error de validación.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        //Se crea un HashMap vacio para almacenar los errores de validación.
        Map<String, String> response = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((err) -> {
            String fieldName = ((FieldError)err).getField();
            String errorMessage = err.getDefaultMessage();

            response.put(fieldName, errorMessage);
        });

        return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
    }


    //
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse>myResourceNotFoundException(ResourceNotFoundException e) {

        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }


    //
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse>myAPIException(APIException e) {

        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }




}
