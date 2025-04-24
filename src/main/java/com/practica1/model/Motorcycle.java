package com.practica1.model;

import com.practica1.model.common.FuelType;

import java.io.Serializable;

public class Motorcycle extends Vehicle implements Serializable {

    private Integer id;
    private Integer vehicleId;
    private Integer concessionaireId;
    private Integer engineDisplacement;// Cilindrada del motor

    public Motorcycle(String brand, String model, Integer year, FuelType fuelType, String licensePlate, Integer engineDisplacement, Integer concessionaireId) {
        super(brand, model, year, fuelType, licensePlate);
        this.engineDisplacement = engineDisplacement;
        this.concessionaireId = concessionaireId;
    }

    public Motorcycle() {
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getConcessionaireId() {
        return concessionaireId;
    }

    public void setConcessionaireId(Integer concessionaireId) {
        this.concessionaireId = concessionaireId;
    }

    public Integer getEngineDisplacement() {
        return engineDisplacement;
    }

    public void setEngineDisplacement(Integer engineDisplacement) {
        this.engineDisplacement = engineDisplacement;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
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
