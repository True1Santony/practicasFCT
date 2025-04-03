package com.practica1.model;

import java.util.*;

public class Concessionaire {

    private int id;
    private String name;
    private List<Vehicle> vehicles;
    private Map<String,Vehicle> vehiclesByLicensePlate;
    private Set<String> uniqueVehicleBrands;

    public Concessionaire() {
        this.vehicles = new ArrayList<>();
        this.vehiclesByLicensePlate = new HashMap<>();
        this.uniqueVehicleBrands = new HashSet<>();
    }
    public Concessionaire(String name){
        this.name = name;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public Map<String, Vehicle> getVehiclesByLicensePlate() {
        return vehiclesByLicensePlate;
    }

    public void setVehiclesByLicensePlate(Map<String, Vehicle> vehiclesByLicensePlate) {
        this.vehiclesByLicensePlate = vehiclesByLicensePlate;
    }

    public Set<String> getUniqueVehicleBrands() {
        return uniqueVehicleBrands;
    }

    public void setUniqueVehicleBrands(Set<String> uniqueVehicleBrands) {
        this.uniqueVehicleBrands = uniqueVehicleBrands;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
