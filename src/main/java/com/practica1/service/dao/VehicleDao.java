package com.practica1.service.dao;

import com.practica1.model.Vehicle;
import com.practica1.service.common.exception.VehicleNotFoundException;

import java.util.List;
import java.util.Optional;

public interface VehicleDao {
    int insert(Vehicle vehicle);
    void update(String licensePlate, Vehicle vehicle);
    void deleteByLicensePlate(String licensePlate);
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    List<Vehicle> getAll();
    List<Vehicle> getById(int id) throws VehicleNotFoundException;
}
