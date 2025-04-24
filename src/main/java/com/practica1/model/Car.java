package com.practica1.model;

import com.practica1.model.common.FuelType;

import java.io.Serializable;

public class Car extends Vehicle implements Serializable {

    private Integer id;
    private Integer vehicleId;
    private Integer concessionaireId;
    private Integer numberOfDoors;

    public Car(String brand, String model, Integer year, FuelType fuelType, String licensePlate, Integer numbrerOfDoors, Integer concessionaireId) {
        super(brand, model, year, fuelType,licensePlate);
        this.numberOfDoors = numbrerOfDoors;
        this.concessionaireId = concessionaireId;
    }

    public Car() {
        super();
    }

    @Override
    public void displayInformation() throws NullPointerException {
        System.out.println("-------------------------------");
        System.out.println("Car Information: ");
        System.out.println("Car ID: " + getId());
        System.out.println("Brand: " + getBrand());
        System.out.println("Model: " + getModel());
        System.out.println("Licence plate: " + getLicensePlate());
        System.out.println("Year: " + getYear());
        System.out.println("Fuel Type: " + getFuelType());
        System.out.println("Number of Doors: " + numberOfDoors);
        System.out.println("Id concesionare: " + getConcessionaireId());
        System.out.println("-------------------------------");
    }

    @Override
    public Integer getId() {
        return id;
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

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(Integer numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }
}
