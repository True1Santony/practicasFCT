package com.practica1.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:h2:file:./concesionario_db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void initializeDatabase() {

        try {
            Class.forName("org.h2.Driver");
            System.out.println("H2 driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading H2 driver: " + e.getMessage());
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            // Create Vehicle table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS Vehicle (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    vehicle_type VARCHAR(50) NOT NULL
                )
            """);

            // Create Car table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS Car (
                    id INT PRIMARY KEY,
                    number_of_doors INT,
                    license_plate VARCHAR(50) UNIQUE NOT NULL,
                    brand VARCHAR(50),
                    model VARCHAR(50),
                    year INT,
                    fuel_type VARCHAR(50),
                    FOREIGN KEY (id) REFERENCES Vehicle(id)
                )
            """);

            // Create Motorcycle table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS Motorcycle (
                    id INT PRIMARY KEY,
                    engine_displacement INT,
                    license_plate VARCHAR(50) UNIQUE NOT NULL,
                    brand VARCHAR(50),
                    model VARCHAR(50),
                    year INT,
                    fuel_type VARCHAR(50),
                    FOREIGN KEY (id) REFERENCES Vehicle(id)
                )
            """);

            // Create Concessionaire table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS Concessionaire (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(100) NOT NULL
                )
            """);

            // Insert initial data if Vehicle table is empty
            if (statement.executeQuery("SELECT COUNT(*) FROM Vehicle").getInt(1) == 0) {
                statement.execute("INSERT INTO Vehicle (vehicleType, licensePlate, brand, model, year, fuelType) VALUES ('CAR', 'TESTCAR1', 'Toyota', 'Corolla', 2020, 'GASOLINE')");
                statement.execute("INSERT INTO Vehicle (vehicleType, licensePlate, brand, model, year, fuelType) VALUES ('MOTORCYCLE', 'TESTMOTO1', 'Honda', 'CBR', 2019, 'GASOLINE')");
            }

        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}
