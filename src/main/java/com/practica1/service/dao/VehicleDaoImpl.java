package com.practica1.service.dao;

import com.practica1.model.Car;
import com.practica1.model.Motorcycle;
import com.practica1.model.Vehicle;

import java.util.Optional;

public class VehicleDaoImpl implements VehicleDao{

    private CarDao carService;
    private MotorcycleDao motorcycleService;
    private VehicleTypeRepository typeRepository;

    public VehicleDaoImpl(CarDao carService, MotorcycleDao motorcycleService, VehicleTypeRepository typeRepository) {
        this.carService = carService;
        this.motorcycleService = motorcycleService;
        this.typeRepository = typeRepository;
    }

    @Override
    public int insert(Vehicle vehicle) {
        if (vehicle instanceof Car) {
            return handleInsert((Car) vehicle, "CAR");
        } else if (vehicle instanceof Motorcycle) {
            return handleInsert((Motorcycle) vehicle, "MOTORCYCLE");
        } else {
            System.out.println("Tipo de vehículo no soportado.");
            return -1;
        }
    }
    private <T extends Vehicle> int handleInsert(T vehicle, String vehicleType) {
        Optional<Integer> vehicleIdOptional = typeRepository.findIdByType(vehicleType);
        int vehicleId;

        if (vehicleIdOptional.isEmpty()) {
            System.out.println("No se encontró un vehicleId para el tipo " + vehicleType + ". Creando uno...");
            vehicleId = typeRepository.insertAndCreateType(vehicleType);

            if (vehicleId == -1) {
                return -1; // Error al crear el vehicleId
            }
        } else {
            vehicleId = vehicleIdOptional.get();
        }

        if (vehicle instanceof Car) {
            return carService.create((Car) vehicle, vehicleId);
        } else if (vehicle instanceof Motorcycle) {
            return motorcycleService.create((Motorcycle) vehicle, vehicleId);
        }

        return -1;
    }


    @Override
    public void update(String licensePlate, Vehicle vehicle) {
        Optional<Car> car = carService.findByLicensePlate(licensePlate);
        Optional<Motorcycle> motorcycle = motorcycleService.findByLicensePlate(licensePlate);

        if (car.isPresent()){
            carService.update((Car)vehicle, car.get().getId());
        } else if (motorcycle.isPresent()){
            motorcycleService.update((Motorcycle)vehicle, motorcycle.get().getId());
        }
    }

    @Override
    public void deleteByLicensePlate(String licensePlate) {
        Optional<Car> car = carService.findByLicensePlate(licensePlate);
        Optional<Motorcycle> motorcycle = motorcycleService.findByLicensePlate(licensePlate);

        if (car.isPresent()){
            carService.deleteById(car.get().getId());
        }else if (motorcycle.isPresent()){
            motorcycleService.deleteById(motorcycle.get().getId());
        }

    }

    @Override
    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        Optional<Car> car = carService.findByLicensePlate(licensePlate);
        if (car.isPresent()){
            return Optional.of(car.get());
        }

        Optional<Motorcycle> motorcycle = motorcycleService.findByLicensePlate(licensePlate);
        if (motorcycle.isPresent()){
            return Optional.of(motorcycle.get());
        }

        return Optional.empty();
    }

}

