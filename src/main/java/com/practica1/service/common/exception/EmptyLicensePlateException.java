package com.practica1.service.common.exception;

public class EmptyLicensePlateException extends Exception{
    public EmptyLicensePlateException(){
        super("No hay vehículos registrados.");
    }
}
