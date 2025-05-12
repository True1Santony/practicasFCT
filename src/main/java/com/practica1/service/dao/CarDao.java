package com.practica1.service.dao;

import com.practica1.model.Car;
import com.practica1.service.common.exception.DuplicateLicensePlateException;

import java.util.List;
import java.util.Optional;

public interface CarDao {
    int create(Car car, int vehicleId) throws DuplicateLicensePlateException;
    void update(Car car, int carId);
    void deleteById(int id);
    Optional<Car> findByLicensePlate(String licensePlate);
    List<Car> findAll();
    Optional <List<Car>> findByVehicleId(int id);
}
