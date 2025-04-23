package com.practica1.service.common.exception;

public class VehicleNotFoundException extends RuntimeException{
    public VehicleNotFoundException(int id){
        super("Vehículo con id: " + id + " no encontrado.");
    }
}
