package com.practica1.service.common;

public class TypeNotFoundException extends Exception{
    public TypeNotFoundException(String type) {
        super("No se han encontrado vehículos del tipo " + type + ".");
    }
}
