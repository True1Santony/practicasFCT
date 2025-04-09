package com.practica1.dao;

import com.practica1.base.DatabaseConnection;
import com.practica1.model.Motorcycle;
import com.practica1.model.common.FuelType;

import java.sql.*;
import java.util.Optional;

public class MotorcycleDao {

    public int create(Motorcycle motorcycle,int vehicleId) {
        String query = "INSERT INTO Motorcycle (id_concessionaire, engine_displacement, license_plate, brand, model, \"year\", fuel_type, vehicle_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1,motorcycle.getConcessionaireId());
            stmt.setInt(2, motorcycle.getEngineDisplacement());
            stmt.setString(3, motorcycle.getLicensePlate());
            stmt.setString(4, motorcycle.getBrand());
            stmt.setString(5, motorcycle.getModel());
            stmt.setInt(6, motorcycle.getYear());
            stmt.setString(7, motorcycle.getFuelType().name());
            stmt.setInt(8, vehicleId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public void update(Motorcycle motorcycle, int motorcycleId) {
        String query = "UPDATE Motorcycle SET engine_displacement = ?, license_plate = ?, brand = ?, model = ?, \"year\" = ?, fuel_type = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, motorcycle.getEngineDisplacement());
            stmt.setString(2, motorcycle.getLicensePlate());
            stmt.setString(3, motorcycle.getBrand());
            stmt.setString(4, motorcycle.getModel());
            stmt.setInt(5, motorcycle.getYear());
            stmt.setString(6, motorcycle.getFuelType().name());
            stmt.setInt(7, motorcycleId); // ID de la moto a actualizar

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Motorcycle updated successfully.");
            } else {
                System.out.println("Motorcycle with id " + motorcycle.getId() + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id) {
        String query = "DELETE FROM Motorcycle WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Motorcycle with id " + id + " deleted successfully.");
            } else {
                System.out.println("Motorcycle with id " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Motorcycle> findBylicensePlate(String licensePlate) {
        String query = "SELECT * FROM Motorcycle WHERE license_plate = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, licensePlate);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Motorcycle motorcycle = new Motorcycle();
                motorcycle.setId(resultSet.getInt("id"));
                motorcycle.setEngineDisplacement(resultSet.getInt("engine_displacement"));
                motorcycle.setLicensePlate(resultSet.getString("license_plate"));
                motorcycle.setBrand(resultSet.getString("brand"));
                motorcycle.setModel(resultSet.getString("model"));
                motorcycle.setYear(resultSet.getInt("year"));
                motorcycle.setFuelType(FuelType.valueOf(resultSet.getString("fuel_type")));
                motorcycle.setVehicleId(resultSet.getInt("vehicle_id"));
                motorcycle.setConcessionaireId(resultSet.getInt("id_concessionaire"));

                return Optional.of(motorcycle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();

    }
}
