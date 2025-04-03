package com.practica1.DAO;

import com.practica1.model.Car;
import com.practica1.model.Motorcycle;
import com.practica1.model.Vehicle;

import java.sql.*;
import java.util.Optional;

public class VehicleDAO implements CrudDAO<Vehicle>{

    private static final String URL = "jdbc:h2:file:./concesionario_db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    @Override
    public int create(Vehicle vehicle) {
        String query;

        // Comprobamos si el vehículo es un coche o una moto
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (vehicle instanceof Car) {
                // Si es un coche, primero insertamos en la tabla Vehicle para obtener el ID
                query = "INSERT INTO Vehicle (vehicle_type) VALUES ('CAR')";
                try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.executeUpdate();

                    // Obtener el ID generado para la tabla Vehicle
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);

                            // Ahora insertamos los detalles del coche en la tabla Car, usando el ID de Vehicle
                            Car car = (Car) vehicle;
                            String carQuery = "INSERT INTO Car (number_of_doors, license_plate, brand, model, \"year\", fuel_type, vehicle_id) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                            try (PreparedStatement carStmt = connection.prepareStatement(carQuery)) {
                                carStmt.setInt(2, car.getNumberOfDoors());
                                carStmt.setString(3, car.getLicensePlate());
                                carStmt.setString(4, car.getBrand());
                                carStmt.setString(5, car.getModel());
                                carStmt.setInt(6, car.getYear());
                                carStmt.setString(7, car.getFuelType().name());
                                carStmt.setInt(8, generatedId); // Relacionar con el ID de Vehicle
                                carStmt.executeUpdate();
                            }
                            return generatedId; // Retornamos el ID del coche insertado
                        }
                    }
                }
            } else if (vehicle instanceof Motorcycle) {
                // Si es una moto, solo insertamos en la tabla Motorcycle sin usar la tabla Vehicle
                query = "INSERT INTO Motorcycle ( engine_displacement, license_plate, brand, model, \"year\", fuel_type) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    Motorcycle motorcycle = (Motorcycle) vehicle;
                    stmt.setInt(2, motorcycle.getEngineDisplacement());
                    stmt.setString(3, motorcycle.getLicensePlate());
                    stmt.setString(4, motorcycle.getBrand());
                    stmt.setString(5, motorcycle.getModel());
                    stmt.setInt(6, motorcycle.getYear());
                    stmt.setString(7, motorcycle.getFuelType().name());
                    stmt.executeUpdate();

                    // Obtener el ID generado para la moto
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1); // Retornamos el ID de la moto insertada
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Unknown vehicle type: " + vehicle.getClass().getSimpleName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return -1;  // Retornamos -1 si ocurrió algún error
    }
    }

    @Override
    public Optional<Vehicle> findById(int id) {
        String query = "SELECT * FROM Vehicle WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String vehicleType = resultSet.getString("vehicle_type");

                Vehicle vehicle = null;

                /

                return Optional.ofNullable(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void update(Vehicle entity) {

    }

    @Override
    public void deleteById(int id) {

    }
}
