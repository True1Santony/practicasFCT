package com.practica1.model;

import com.practica1.model.common.FuelType;

import java.io.Serializable;

public class Motorcycle extends Vehicle implements Serializable {

    private int id;
    private int vehicleId;
    private int concessionaireId;
    private int engineDisplacement;// Cilindrada del motor

    public Motorcycle(String brand, String model, int year, FuelType fuelType, String licensePlate, int engineDisplacement, int concessionaireId) {
        super(brand, model, year, fuelType, licensePlate);
        this.engineDisplacement = engineDisplacement;
        this.concessionaireId = concessionaireId;
    }

    public Motorcycle() {
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getConcessionaireId() {
        return concessionaireId;
    }

    public void setConcessionaireId(int concessionaireId) {
        this.concessionaireId = concessionaireId;
    }

    public int getEngineDisplacement() {
        return engineDisplacement;
    }

    public void setEngineDisplacement(int engineDisplacement) {
        this.engineDisplacement = engineDisplacement;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void displayInformation() throws NullPointerException{
        System.out.println("-------------------------------");
        System.out.println("Motorcycle Information: ");
        System.out.println("ID: " + getId());
        System.out.println("Brand: " + getBrand());
        System.out.println("Licence plate: " + getLicensePlate());
        System.out.println("Model: " + getModel());
        System.out.println("Year: " + getYear());
        System.out.println("Fuel Type: " + getFuelType());
        System.out.println("Engine Displacement: " + engineDisplacement + " cc");
        System.out.println("-------------------------------");
    }
}
