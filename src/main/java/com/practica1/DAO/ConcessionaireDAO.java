package com.practica1.DAO;

import com.practica1.model.Concessionaire;

import java.sql.*;

public class ConcessionaireDAO {

    private static final String URL = "jdbc:h2:file:./concesionario_db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // Método para crear un concesionario
    public int create(Concessionaire concessionaire) {
        String query = "INSERT INTO Concessionaire (name) VALUES (?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, concessionaire.getName());

            stmt.executeUpdate();

            // Obtener el id autogenerado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    concessionaire.setId(generatedKeys.getInt(1)); //ID generado al objeto Concessionaire
                    return concessionaire.getId();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Si ocurrió un error, retornar -1
    }

    // Método para encontrar un concesionario por su ID
    public Concessionaire findById(int id) {
        String query = "SELECT * FROM Concessionaire WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Concessionaire concessionaire = new Concessionaire();
                concessionaire.setId(resultSet.getInt("id"));
                concessionaire.setName(resultSet.getString("name"));
                return concessionaire;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra el concesionario, retornar null
    }

    // Método para actualizar un concesionario
    public void update(Concessionaire concessionaire) {
        String query = "UPDATE Concessionaire SET name = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, concessionaire.getName());
            stmt.setInt(2, concessionaire.getId());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Concessionaire updated successfully.");
            } else {
                System.out.println("Concessionaire with id " + concessionaire.getId() + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un concesionario por su ID
    public void deleteById(int id) {
        String query = "DELETE FROM Concessionaire WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Concessionaire with id " + id + " deleted successfully.");
            } else {
                System.out.println("Concessionaire with id " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
