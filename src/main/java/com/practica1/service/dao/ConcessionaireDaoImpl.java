package com.practica1.service.dao;

import com.practica1.base.DatabaseConnection;
import com.practica1.model.Car;
import com.practica1.model.Concessionaire;
import com.practica1.model.Motorcycle;

import java.sql.*;

public class ConcessionaireDaoImpl implements ConcessionaireDao {

    private final VehicleDao vehicleService;
    private final DatabaseConnection databaseConnection;

    public ConcessionaireDaoImpl(VehicleDao vehicleService, DatabaseConnection databaseConnection) {
        this.vehicleService = vehicleService;
        this.databaseConnection = databaseConnection;
    }

    @Override
    public int insert(Concessionaire concessionaire) {
        String query = "INSERT INTO Concessionaire (name) VALUES (?)";
        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

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

    @Override
    public Concessionaire findById(int id) {
        String query = "SELECT * FROM Concessionaire WHERE id = ?";
        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query)) {

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

    @Override
    public void infoConcessionareByLicencePlate(String licencePlate){
        vehicleService.findByLicensePlate(licencePlate)
                .ifPresent(vehicle -> {
                    if (vehicle instanceof Car) {
                        Car car = (Car) vehicle;
                        findById(car.getConcessionaireId())
                                .displayInformation();
                    } else if (vehicle instanceof Motorcycle) {
                        Motorcycle motorcycle = (Motorcycle) vehicle;
                        findById(motorcycle.getConcessionaireId())
                                .displayInformation();
                    } else {
                        System.out.println("El vehículo encontrado no es ni un coche ni una moto.");
                    }
                });
    }
}
