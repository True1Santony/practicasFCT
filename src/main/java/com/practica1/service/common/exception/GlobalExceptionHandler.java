package com.practica1.service.common.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ApiError> handleVehicleNotFoundException(VehicleNotFoundException ex) {
        log.warn(ex.getMessage());
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(InvalidTypeIdException.class)
    public ResponseEntity<ApiError> handleInvalidTypeIdException(InvalidTypeIdException ex) {
        log.warn("Tipo de vehículo inválido: {}", ex.getMessage());
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Campo 'type' no proporcionado o inválido. Debe ser 'car' o 'motorcycle'."
        );
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(DuplicateLicensePlateException.class)
    public ResponseEntity<ApiError> handleDuplicateLicensePlateException(DuplicateLicensePlateException ex) {
        log.warn("Matrícula duplicada: {}", ex.getMessage());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiError> handleNullPointerException(NullPointerException ex) {
        log.error("NullPointerException capturado: {}", ex.getMessage());
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Faltan campos obligatorios en la solicitud. Por favor, asegúrate de que todos los campos requeridos estén completos."
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(EmptyLicensePlateException.class)
    public ResponseEntity<ApiError> handleEmptyLicensePlateException(EmptyLicensePlateException ex) {
        log.warn("Matrícula vacía o inválida: {}", ex.getMessage());
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Se requiere una matrícula de vehiculo válida"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<ApiError> handleClassCastException(ClassCastException ex) {
        log.warn("Intento de conversión inválida: {}", ex.getMessage());
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "No puede convertir un coche en moto y viceversa"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Parámetro inválido: {}", ex.getMessage());
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Parámetro inválido."
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidField(Exception ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage())
        );
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ApiError> handleJsonParseError(JsonParseException ex) {
        log.error("Error al parsear JSON: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiError(HttpStatus.BAD_REQUEST.value(), "Formato JSON inválido.")
        );
    }

}

