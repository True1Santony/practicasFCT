package com.practica1.model;

import java.util.*;

public class Concessionaire {
    private List<Vehicle> vehicles;
    private Map<String,Vehicle> vehiclesByLicensePlate;
    private Set<String> uniqueVehicleBrands;

    public Concessionaire() {
        this.vehicles = new ArrayList<>();
        this.vehiclesByLicensePlate = new HashMap<>();
        this.uniqueVehicleBrands = new HashSet<>();
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
}
