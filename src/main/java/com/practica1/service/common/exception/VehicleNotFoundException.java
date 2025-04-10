package com.practica1.service.common.exception;

public class VehicleNotFoundException extends Exception{
    public VehicleNotFoundException(String licensePlate){
        super("Vehículo con matrícula " + licensePlate + " no encontrado.");
    }
}
