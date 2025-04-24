package com.practica1.service.dao;

import com.practica1.model.Car;
import com.practica1.model.Motorcycle;
import com.practica1.model.Vehicle;
import com.practica1.service.common.exception.EmptyLicensePlateException;
import com.practica1.service.common.exception.VehicleNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
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

        if (car.isEmpty() && motorcycle.isEmpty()) {
            throw new EmptyLicensePlateException();
        }

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

    @Override
    public List<Vehicle> getAll() {
        List<Car> cars = carService.findAll();
        List<Motorcycle> motorcycles = motorcycleService.findAll();

        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.addAll(cars);
        vehicles.addAll(motorcycles);

        return vehicles;
    }

    @Override
    public List<Vehicle> getById(int id) throws VehicleNotFoundException {
        List<Vehicle> vehicles = new ArrayList<>();

        Optional<List<Car>> carOptional = carService.findByVehicleId(id);
        if (carOptional.isPresent()) {
            vehicles.addAll(carOptional.get());
            return vehicles;
        }

        Optional<List<Motorcycle>> motorcycleOptional = motorcycleService.findByVehicleId(id);
        if (motorcycleOptional.isPresent()) {
            vehicles.addAll(motorcycleOptional.get());
            return vehicles;
        }

        throw new VehicleNotFoundException(id);
    }

    @Override
    public List<Vehicle> filter(Vehicle filter) {
        List<Vehicle> allVehicles = getAll();

        return allVehicles.stream()
                .filter(vehicle -> {
                    if (filter instanceof Car && vehicle instanceof Car) {
                        Car filterCar = (Car) filter;
                        Car car = (Car) vehicle;
                        return (filterCar.getId() == null || filterCar.getId().equals(car.getId())) &&
                                (filterCar.getBrand() == null || filterCar.getBrand().equalsIgnoreCase(car.getBrand())) &&
                                (filterCar.getModel() == null || filterCar.getModel().equalsIgnoreCase(car.getModel())) &&
                                (filterCar.getYear() == null || filterCar.getYear().equals(car.getYear())) &&
                                (filterCar.getFuelType() == null || filterCar.getFuelType().equals(car.getFuelType())) &&
                                (filterCar.getVehicleId() == null || filterCar.getVehicleId().equals(car.getVehicleId())) &&
                                (filterCar.getLicensePlate() == null || filterCar.getLicensePlate().equalsIgnoreCase(car.getLicensePlate())) &&
                                (filterCar.getNumberOfDoors() == null || filterCar.getNumberOfDoors().equals(car.getNumberOfDoors())) &&
                                (filterCar.getConcessionaireId() == null || filterCar.getConcessionaireId().equals(car.getConcessionaireId()));
                    }

                    if (filter instanceof Motorcycle && vehicle instanceof Motorcycle) {
                        Motorcycle filterMoto = (Motorcycle) filter;
                        Motorcycle moto = (Motorcycle) vehicle;
                        return (filterMoto.getId() == null || filterMoto.getId().equals(moto.getId())) &&
                                (filterMoto.getBrand() == null || filterMoto.getBrand().equalsIgnoreCase(moto.getBrand())) &&
                                (filterMoto.getYear() == null || filterMoto.getYear().equals(moto.getYear())) &&
                                (filterMoto.getModel() == null || filterMoto.getModel().equalsIgnoreCase(moto.getModel())) &&
                                (filterMoto.getFuelType() == null || filterMoto.getFuelType().equals(moto.getFuelType())) &&
                                (filterMoto.getVehicleId() == null || filterMoto.getVehicleId().equals(moto.getVehicleId())) &&
                                (filterMoto.getLicensePlate() == null || filterMoto.getLicensePlate().equalsIgnoreCase(moto.getLicensePlate())) &&
                                (filterMoto.getEngineDisplacement() == null || filterMoto.getEngineDisplacement().equals(moto.getEngineDisplacement())) &&
                                (filterMoto.getConcessionaireId() == null || filterMoto.getConcessionaireId().equals(moto.getConcessionaireId()));
                    }

                    return false;
                })
                .toList();
    }

}

