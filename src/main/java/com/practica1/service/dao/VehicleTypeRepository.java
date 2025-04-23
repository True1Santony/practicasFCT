package com.practica1.service.dao;

import com.practica1.base.DatabaseConnection;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class VehicleTypeRepository {

    private final DatabaseConnection databaseConnection;

    public VehicleTypeRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public Optional<Integer> findIdByType(String vehicleType) {
        String query = "SELECT id FROM Vehicle WHERE vehicle_type = ?";
        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, vehicleType);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public int insertAndCreateType(String vehicleType) {
        String query = "INSERT INTO Vehicle (vehicle_type) VALUES (?)";
        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, vehicleType);
            stmt.executeUpdate();
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
}
