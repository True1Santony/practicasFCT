package com.practica1.service.dao;

import com.practica1.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarDao {
    int create(Car car, int vehicleId);
    void update(Car car, int carId);
    void deleteById(int id);
    Optional<Car> findByLicensePlate(String licensePlate);
    List<Car> findAll();
    List<Car> findByConcessionaireId(int concessionaireId);
}
