package com.practica1.service.common.exception;

public class EmptyLicensePlateException extends RuntimeException{
    public EmptyLicensePlateException(){
        super("Proporcione la matrícula.");
    }
}
