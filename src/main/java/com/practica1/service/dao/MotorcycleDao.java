package com.practica1.service.dao;

import com.practica1.model.Motorcycle;

import java.util.List;
import java.util.Optional;

public interface MotorcycleDao {
    int create(Motorcycle motorcycle, int vehicleId);
    void update(Motorcycle motorcycle, int id);
    void deleteById(int id);
    Optional<Motorcycle> findByLicensePlate(String licensePlate);
    List<Motorcycle> findAll();
    Optional<List<Motorcycle>> findByVehicleId(int id);
}
