package com.practica1.service.common.exception;

public class DuplicateLicensePlateException extends RuntimeException{
    public DuplicateLicensePlateException(String licencePlate){
        super("La matrícula " + licencePlate + " ya está registrada.");
    }
}
