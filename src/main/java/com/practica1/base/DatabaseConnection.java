package com.practica1.base;

import java.io.File;
import java.sql.*;
import java.util.Scanner;

public class DatabaseConnection {

    private static final String URL = "jdbc:h2:file:./concesionario_db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final String FILEPATH = "./concesionario_db.mv.db";

    public void initializeDatabase() {

        deleteDatabaseFile(FILEPATH);

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

    private static void deleteDatabaseFile(String FILEPATH) {
        // Verificamos si el archivo existe
        File dbFile = new File(FILEPATH);

        if (!dbFile.exists()) {
            System.out.println("El archivo de la base de datos no existe.");
            return;
        }

        // Preguntar al usuario si quiere eliminar el archivo
        Scanner scanner = new Scanner(System.in);
        int userChoice = -1; // Valor inicial fuera del rango de opciones válidas

        // Bucle que continuará hasta que el usuario ingrese una opción válida
        while (userChoice != 1 && userChoice != 2) {
            System.out.println("¿Estás seguro de que quieres eliminar el archivo de la base de datos?");
            System.out.println("1: Sí, eliminar.");
            System.out.println("2: No, cancelar.");

            // Comprobar que la entrada es un número entero
            if (scanner.hasNextInt()) {
                userChoice = scanner.nextInt();
                if (userChoice == 1) {
                    // Intentamos eliminar el archivo
                    if (dbFile.delete()) {
                        System.out.println("El archivo ha sido eliminado correctamente.");
                    } else {
                        System.out.println("Hubo un error al intentar eliminar el archivo.");
                    }
                } else if (userChoice == 2) {
                    System.out.println("Operación cancelada. El archivo no fue eliminado.");
                } else {
                    System.out.println("Opción no válida. Por favor ingrese 1 o 2.");
                }
            } else {
                // Si la entrada no es un número entero
                System.out.println("Entrada no válida. Por favor ingrese un número entero (1 o 2).");
                scanner.next(); // Limpiar el buffer del scanner
            }
        }

        // Cerrar el scanner
        scanner.close();
    }


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
