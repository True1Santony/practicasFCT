package com.practica1.DAO;

import com.practica1.base.DatabaseConnection;
import com.practica1.model.Car;
import com.practica1.model.common.FuelType;

import java.sql.*;
import java.util.Optional;

public class CarDAO {

    private final VehicleDAO vehicleService = new VehicleDAO();

    public int create(Car car) {

        // Obtener el vehicleId correspondiente al tipo "CAR"
        Optional<Integer> vehicleIdOptional = vehicleService.findIdByType("CAR");
        if (vehicleIdOptional.isEmpty()) {
            System.out.println("No se encontró un vehicleId para el tipo CAR. Creando uno...");
            int newVehicleId = vehicleService.create("CAR");
            if (newVehicleId == -1) {
                System.out.println("Error al crear el vehicleId para CAR.");
                return -1;
            }
            car.setVehicleId(newVehicleId);
        } else {
            car.setVehicleId(vehicleIdOptional.get());
        }

            String query = "INSERT INTO Car (number_of_doors, license_plate, brand, model, \"year\", fuel_type, vehicle_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(query)) {

                // Establecer los valores para los campos del coche (sin el id)
                stmt.setInt(1, car.getNumberOfDoors());
                stmt.setString(2, car.getLicensePlate());
                stmt.setString(3, car.getBrand());
                stmt.setString(4, car.getModel());
                stmt.setInt(5, car.getYear());
                stmt.setString(6, car.getFuelType().name());
                stmt.setInt(7, car.getVehicleId());

                stmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
    }

    public Optional<Car> findById(int id) {
        String query = "SELECT * FROM Car WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt("id"));
                car.setNumberOfDoors(resultSet.getInt("number_of_doors"));
                car.setLicensePlate(resultSet.getString("license_plate"));
                car.setBrand(resultSet.getString("brand"));
                car.setModel(resultSet.getString("model"));
                car.setYear(resultSet.getInt("year"));
                car.setFuelType(FuelType.ELECTRIC);
                car.setVehicleId(resultSet.getInt("vehicle_id"));

                return Optional.of(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void update(Car car) {
        String query = "UPDATE Car SET number_of_doors = ?, license_plate = ?, brand = ?, model = ?, \"year\" = ?, fuel_type = ?, vehicle_id = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Establecer los valores para cada campo del coche
            stmt.setInt(1, car.getNumberOfDoors());
            stmt.setString(2, car.getLicensePlate());
            stmt.setString(3, car.getBrand());
            stmt.setString(4, car.getModel());
            stmt.setInt(5, car.getYear());
            stmt.setString(6, car.getFuelType().name());
            stmt.setInt(7, car.getVehicleId()); // Relación con la tabla Vehicle
            stmt.setInt(8, car.getId()); // ID del coche a actualizar

            // Ejecutar la actualización
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Car updated successfully.");
            } else {
                System.out.println("Car with id " + car.getId() + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteById(int id) {
        String query = "DELETE FROM Car WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Car with id " + id + " deleted successfully.");
            } else {
                System.out.println("Car with id " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Car> findBylicensePlate(String licensePlate) {
        String query = "SELECT * FROM Car WHERE license_plate = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, licensePlate);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt("id"));
                car.setNumberOfDoors(resultSet.getInt("number_of_doors"));
                car.setLicensePlate(resultSet.getString("license_plate"));
                car.setBrand(resultSet.getString("brand"));
                car.setModel(resultSet.getString("model"));
                car.setYear(resultSet.getInt("year"));
                car.setFuelType(FuelType.valueOf(resultSet.getString("fuel_type")));
                car.setVehicleId(resultSet.getInt("vehicle_id"));

                return Optional.of(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
