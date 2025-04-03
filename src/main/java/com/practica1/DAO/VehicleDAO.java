package com.practica1.DAO;

import com.practica1.base.DatabaseConnection;

import java.sql.*;
import java.util.Optional;

public class VehicleDAO {

    public int create(String vehicleType) {
        String query = "INSERT INTO Vehicle (vehicle_type) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, vehicleType);
            stmt.executeUpdate();

            // Obtener el ID generado para el vehículo
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Retornamos el ID del vehículo insertado
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Optional<String> findById(int id) {
        String query = "SELECT vehicle_type FROM Vehicle WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String vehicleType = resultSet.getString("vehicle_type");
                return Optional.of(vehicleType); // Retorna el tipo de vehículo
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty(); // Si no se encuentra el vehículo, retornamos un Optional vacío
    }

    public void update(int id, String vehicleType) {
        String query = "UPDATE Vehicle SET vehicle_type = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Establecemos el valor para el campo vehicle_type y el id del vehículo
            stmt.setString(1, vehicleType);
            stmt.setInt(2, id);

            // Ejecutamos la consulta de actualización
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Vehicle updated successfully.");
            } else {
                System.out.println("Vehicle with id " + id + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id) {
        String query = "DELETE FROM Vehicle WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Vehicle with id " + id + " deleted successfully.");
            } else {
                System.out.println("Vehicle with id " + id + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
}

