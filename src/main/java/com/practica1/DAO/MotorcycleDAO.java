package com.practica1.DAO;

import com.practica1.model.Motorcycle;
import com.practica1.model.common.FuelType;

import java.sql.*;

public class MotorcycleDAO {
    private static final String URL = "jdbc:h2:file:./concesionario_db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // Método para crear una motocicleta
    public int create(Motorcycle motorcycle) {
        String query = "INSERT INTO Motorcycle (engine_displacement, license_plate, brand, model, \"year\", fuel_type) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, motorcycle.getEngineDisplacement());
            stmt.setString(2, motorcycle.getLicensePlate());
            stmt.setString(3, motorcycle.getBrand());
            stmt.setString(4, motorcycle.getModel());
            stmt.setInt(5, motorcycle.getYear());
            stmt.setString(6, motorcycle.getFuelType().name());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    motorcycle.setId(generatedKeys.getInt(1));  // Asignamos el ID generado al objeto Motorcycle
                    return motorcycle.getId();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Si ocurrió un error, retornar -1
    }

    // Método para encontrar una motocicleta por su ID
    public Motorcycle findById(int id) {
        String query = "SELECT * FROM Motorcycle WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Motorcycle motorcycle = new Motorcycle(
                        resultSet.getString("brand"),
                        resultSet.getString("model"),
                        resultSet.getInt("year"),
                        FuelType.valueOf(resultSet.getString("fuel_type")),
                        resultSet.getString("license_plate"),
                        resultSet.getInt("engine_displacement")
                );
                motorcycle.setId(resultSet.getInt("id"));
                return motorcycle;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra la motocicleta, retornar null
    }

    // Método para actualizar una motocicleta
    public void update(Motorcycle motorcycle) {
        String query = "UPDATE Motorcycle SET engine_displacement = ?, license_plate = ?, brand = ?, model = ?, \"year\" = ?, fuel_type = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, motorcycle.getEngineDisplacement());
            stmt.setString(2, motorcycle.getLicensePlate());
            stmt.setString(3, motorcycle.getBrand());
            stmt.setString(4, motorcycle.getModel());
            stmt.setInt(5, motorcycle.getYear());
            stmt.setString(6, motorcycle.getFuelType().name());
            stmt.setInt(7, motorcycle.getId()); // ID de la moto a actualizar

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

    // Método para eliminar una motocicleta por su ID
    public void deleteById(int id) {
        String query = "DELETE FROM Motorcycle WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
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
}
