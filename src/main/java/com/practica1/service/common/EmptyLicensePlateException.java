package com.practica1.service.common;

public class EmptyLicensePlateException extends Exception{
    public EmptyLicensePlateException(){
        super("No hay vehículos registrados.");
    }
}
