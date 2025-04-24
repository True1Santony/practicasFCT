package com.practica1.service.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ApiError> handleVehicleNotFoundException(VehicleNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(InvalidTypeIdException.class)
    public ResponseEntity<ApiError> handleInvalidTypeIdException(InvalidTypeIdException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Campo 'type' no proporcionado o inválido. Debe ser 'car' o 'motorcycle'."
        );
        return ResponseEntity.badRequest().body(apiError);
    }
    @ExceptionHandler(DuplicateLicensePlateException.class)
    public ResponseEntity<ApiError> handleDuplicateLicensePlateException(DuplicateLicensePlateException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiError> handleNullPointerException(NullPointerException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Faltan campos obligatorios en la solicitud. Por favor, asegúrate de que todos los campos requeridos estén completos."
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(EmptyLicensePlateException.class)
    public ResponseEntity<ApiError> handleEmptyLicensePlateException(EmptyLicensePlateException ex){
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "El campo de la matrícula no puede estar vacio"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<ApiError> handleClassCastException(ClassCastException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "No puede convertir un coche en moto y viceversa"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}

