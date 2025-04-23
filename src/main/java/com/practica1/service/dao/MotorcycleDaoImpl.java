package com.practica1.service.dao;

import com.practica1.base.DatabaseConnection;
import com.practica1.model.Motorcycle;
import com.practica1.model.common.FuelType;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MotorcycleDaoImpl implements MotorcycleDao {

    private final DatabaseConnection databaseConnection;

    public MotorcycleDaoImpl(DatabaseConnection databaseConnection) throws SQLException {
        this.databaseConnection = databaseConnection;
    }
    @Override
    public int create(Motorcycle motorcycle,int vehicleId) {
        String query = "INSERT INTO Motorcycle (id_concessionaire, engine_displacement, license_plate, brand, model, \"year\", fuel_type, vehicle_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

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

    @Override
    public void update(Motorcycle motorcycle, int motorcycleId) {
        String query = "UPDATE Motorcycle SET engine_displacement = ?, license_plate = ?, brand = ?, model = ?, \"year\" = ?, fuel_type = ? WHERE id = ?";

        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query)) {

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

    @Override
    public void deleteById(int id) {
        String query = "DELETE FROM Motorcycle WHERE id = ?";

        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query)) {

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

    @Override
    public Optional<Motorcycle> findByLicensePlate(String licensePlate) {
        String query = "SELECT * FROM Motorcycle WHERE license_plate = ?";
        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query)) {

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

    @Override
    public List<Motorcycle> findAll() {
        List<Motorcycle> motorcycles = new ArrayList<>();
        String query = "SELECT * FROM Motorcycle";

        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query)) {

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
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

                motorcycles.add(motorcycle);  // Añadir el coche a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return motorcycles;  // Retornar la lista con todos los coches
    }

    @Override
    public Optional<List<Motorcycle>> findByVehicleId(int id) {
        List<Motorcycle> motorcycles = new ArrayList<>();
        String query = "SELECT * FROM Motorcycle WHERE vehicle_id = ?";

        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
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

                motorcycles.add(motorcycle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(motorcycles.isEmpty() ? null : motorcycles);
    }
}
