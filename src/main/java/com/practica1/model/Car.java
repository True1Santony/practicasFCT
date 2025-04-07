package com.practica1.model;

import com.practica1.model.common.FuelType;

public class Car extends Vehicle{

    private int id;
    private int vehicleId;
    private int concessionaireId;
    private int numberOfDoors;

    public Car(String brand, String model, int year, FuelType fuelType, String licensePlate, int numbrerOfDoors, int concessionaireId) {
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
    public int getId() {
        return id;
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

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(int numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public int getVehicleId() {
        return vehicleId;
    }
}
