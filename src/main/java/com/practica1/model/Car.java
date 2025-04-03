package com.practica1.model;

import com.practica1.model.common.FuelType;

public class Car extends Vehicle{

    private int numberOfDoors;

    public Car(String brand, String model, int year, FuelType fuelType, String licensePlate, int numbrerOfDoors) {
        super(brand, model, year, fuelType,licensePlate);
        this.numberOfDoors = numbrerOfDoors;
    }

    @Override
    public void displayInformation() {
        System.out.println("-------------------------------");
        System.out.println("Car Information: ");
        System.out.println("Brand: " + getBrand());
        System.out.println("Model: " + getModel());
        System.out.println("Licence plate: " + getLicensePlate());
        System.out.println("Year: " + getYear());
        System.out.println("Fuel Type: " + getFuelType());
        System.out.println("Number of Doors: " + numberOfDoors);
        System.out.println("-------------------------------");
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(int numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }
}
