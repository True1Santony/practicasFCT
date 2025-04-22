package com.practica1.base;

import java.io.File;
import java.sql.*;
import java.util.Scanner;

public class DatabaseConnectionImpl implements DatabaseConnection {

    private static final String URL = "jdbc:h2:file:./concesionario_db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    @Override
    public void initializeDatabase() {

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            // Crear la tabla Vehicle
            statement.execute("CREATE TABLE IF NOT EXISTS Vehicle (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "vehicle_type VARCHAR(50) NOT NULL" +
                    ")");

            // Crear la tabla Concessionaire
            statement.execute("CREATE TABLE IF NOT EXISTS Concessionaire (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(100) NOT NULL" +
                    ")");

            // Crear la tabla Car
            statement.execute("CREATE TABLE IF NOT EXISTS Car (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "id_concessionaire INT," +
                    "number_of_doors INT, " +
                    "license_plate VARCHAR(50) UNIQUE NOT NULL, " +
                    "brand VARCHAR(50), " +
                    "model VARCHAR(50), " +
                    "\"year\" INT, " +  // Escapar la palabra reservada "year"
                    "fuel_type VARCHAR(50), " +
                    "vehicle_id INT, " +
                    "FOREIGN KEY (vehicle_id) REFERENCES Vehicle(id)" +
                    ")");

            // Crear la tabla Motorcycle
            statement.execute("CREATE TABLE IF NOT EXISTS Motorcycle (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "id_concessionaire INT," +
                    "engine_displacement INT, " +
                    "license_plate VARCHAR(50) UNIQUE NOT NULL, " +
                    "brand VARCHAR(50), " +
                    "vehicle_id INT, " +
                    "model VARCHAR(50), " +
                    "\"year\" INT, " +  // Escapar la palabra reservada "year"
                    "fuel_type VARCHAR(50)" +
                    ")");


            // Insertar datos iniciales si la tabla Vehicle está vacía
            String countQuery = "SELECT COUNT(*) FROM Vehicle";
            try (ResultSet rs = statement.executeQuery(countQuery)) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count == 0) {  // Si no hay datos en la tabla
                        statement.execute("INSERT INTO Vehicle (vehicle_type) VALUES ('CAR')");
                        statement.execute("INSERT INTO Vehicle (vehicle_type) VALUES ('MOTORCYCLE')");

                        statement.execute("""
                            INSERT INTO Motorcycle (engine_displacement, license_plate, brand, model, "year", fuel_type, vehicle_id)
                            VALUES (600, 'TESTMOTO1', 'Honda', 'CBR', 2019, 'GASOLINE', 2)
                        """);

                        statement.execute("""
                            INSERT INTO Car (number_of_doors, license_plate, brand, model, "year", fuel_type, vehicle_id)
                            VALUES (5,'TESTCOCHE1','SEAT', 'IBIZA', 2019, 'GASOLINE', 1)
                        """);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
