package com.practica1.dao;

import com.practica1.base.DatabaseConnection;
import com.practica1.model.Car;
import com.practica1.model.Motorcycle;
import com.practica1.model.Vehicle;

import java.sql.*;
import java.util.Optional;

public class VehicleDao {

    private static final CarDao carService = new CarDao();
    private static final MotorcycleDao motorcycleService = new MotorcycleDao();

    public int create(Vehicle vehicle){

        if (vehicle instanceof Car){
            Optional<Integer> vehicleIdOptional = findIdByType("CAR");
            int vehicleId;

            // Si no se encuentra el vehicleId, se crea uno
            if(vehicleIdOptional.isEmpty()){

                System.out.println("No se encontró un vehicleId para el tipo CAR. Creando uno...");
                vehicleId = createType("CAR");

                    if (vehicleId == -1) {
                        return -1; // Error al crear el vehicleId
                    }

                } else {
                vehicleId = vehicleIdOptional.get();
                }

            //si es un coche delega la creacion a carDAO
            return carService.create((Car) vehicle, vehicleId);

        } else if (vehicle instanceof Motorcycle) {

            Optional<Integer> vehicleIdOptional = findIdByType("MOTORCYCLE");
            int vehicleId;

            // Si no se encuentra el vehicleId, se crea uno
            if (vehicleIdOptional.isEmpty()) {

                System.out.println("No se encontró un vehicleId para el tipo MOTORCYCLE. Creando uno...");
                vehicleId = createType("MOTORCYCLE");

                if (vehicleId == -1) {
                    return -1; // Error al crear el vehicleId
                }
                } else {
                    vehicleId = vehicleIdOptional.get();
                }
            return motorcycleService.create((Motorcycle) vehicle, vehicleId);
        } else {

            System.out.println("Tipo de vehículo no soportado.");
            return -1;
        }
    }

    public int createType(String vehicleType) {
        String query = "INSERT INTO Vehicle (vehicle_type) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, vehicleType);
            stmt.executeUpdate();

            // Obtener el ID generado para el vehículo por motorcycle que n otiene relaccion
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void update(String licensePlate, Vehicle vehicle) {
        Optional<Car> car = carService.findBylicensePlate(licensePlate);
        Optional<Motorcycle> motorcycle = motorcycleService.findBylicensePlate(licensePlate);

        if (car.isPresent()){
            carService.update((Car)vehicle, car.get().getId());
        }

        if (motorcycle.isPresent()){
            motorcycleService.update((Motorcycle)vehicle, motorcycle.get().getId());
        }
    }

    public Optional<Integer> findIdByType(String vehicleType) {
        String query = "SELECT id FROM Vehicle WHERE vehicle_type = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, vehicleType);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return Optional.of(resultSet.getInt("id")); // Devolvemos el ID del vehículo encontrado
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty(); // Si no se encuentra, devolvemos un Optional vacío
    }

    public void deleteByLicensePlate(String licensePlate) {

        Optional<Car> car = carService.findBylicensePlate(licensePlate);
        Optional<Motorcycle> motorcycle = motorcycleService.findBylicensePlate(licensePlate);

        if (car.isPresent()){
            carService.deleteById(car.get().getId());
        }

        if (motorcycle.isPresent()){
            motorcycleService.deleteById(motorcycle.get().getId());
        }

    }

    public Optional<Vehicle> findByLicensePlate(String licensePlate) {

        Optional<Car> car = carService.findBylicensePlate(licensePlate);
        Optional<Motorcycle> motorcycle = motorcycleService.findBylicensePlate(licensePlate);

        if (car.isPresent()){
            return Optional.of(car.get());
        }

        if (motorcycle.isPresent()){
            return Optional.of(motorcycle.get());
        }

        return Optional.empty();
    }
}

