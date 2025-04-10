package com.practica1.service.dao;

import com.practica1.model.Car;
import com.practica1.model.common.FuelType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarDao {

    public int create(Car car, int vehicleId, Connection connection) {

        car.setVehicleId(vehicleId);

        String query = "INSERT INTO Car (number_of_doors, license_plate, brand, model, \"year\", fuel_type, vehicle_id, id_concessionaire) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, car.getNumberOfDoors());
            stmt.setString(2, car.getLicensePlate());
            stmt.setString(3, car.getBrand());
            stmt.setString(4, car.getModel());
            stmt.setInt(5, car.getYear());
            stmt.setString(6, car.getFuelType().name());
            stmt.setInt(7, car.getVehicleId());
            stmt.setInt(8, car.getConcessionaireId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public void update(Car car, int carId, Connection connection) {
        String query = "UPDATE Car SET number_of_doors = ?, license_plate = ?, brand = ?, model = ?, \"year\" = ?, fuel_type = ?, vehicle_id = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            // Establecer los valores para cada campo del coche
            stmt.setInt(1, car.getNumberOfDoors());
            stmt.setString(2, car.getLicensePlate());
            stmt.setString(3, car.getBrand());
            stmt.setString(4, car.getModel());
            stmt.setInt(5, car.getYear());
            stmt.setString(6, car.getFuelType().name());
            stmt.setInt(7, car.getVehicleId()); // Relación con la tabla Vehicle
            stmt.setInt(8, carId); // ID del coche a actualizar

            // Ejecutar la actualización
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Car updated successfully.");
            } else {
                System.out.println("Car with id " + car.getId() + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id, Connection connection) {
        String query = "DELETE FROM Car WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Car with id " + id + " deleted successfully.");
            } else {
                System.out.println("Car with id " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Car> findBylicensePlate(String licensePlate, Connection connection) {
        String query = "SELECT * FROM Car WHERE license_plate = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, licensePlate);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt("id"));
                car.setNumberOfDoors(resultSet.getInt("number_of_doors"));
                car.setLicensePlate(resultSet.getString("license_plate"));
                car.setBrand(resultSet.getString("brand"));
                car.setModel(resultSet.getString("model"));
                car.setYear(resultSet.getInt("year"));
                car.setFuelType(FuelType.valueOf(resultSet.getString("fuel_type")));
                car.setVehicleId(resultSet.getInt("vehicle_id"));
                car.setConcessionaireId(resultSet.getInt("id_concessionaire"));

                return Optional.of(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Car> findAll(Connection connection) {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM Car";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt("id"));
                car.setNumberOfDoors(resultSet.getInt("number_of_doors"));
                car.setLicensePlate(resultSet.getString("license_plate"));
                car.setBrand(resultSet.getString("brand"));
                car.setModel(resultSet.getString("model"));
                car.setYear(resultSet.getInt("year"));
                car.setFuelType(FuelType.valueOf(resultSet.getString("fuel_type")));
                car.setVehicleId(resultSet.getInt("vehicle_id"));
                car.setConcessionaireId(resultSet.getInt("id_concessionaire"));

                cars.add(car);  // Añadir el coche a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;  // Retornar la lista con todos los coches
    }

    public List<Car> findByConcessionaireId(int concessionaireId, Connection connection) {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM Car WHERE id_concessionaire = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, concessionaireId);  // Establecer el id del concesionario como parámetro
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt("id"));
                car.setNumberOfDoors(resultSet.getInt("number_of_doors"));
                car.setLicensePlate(resultSet.getString("license_plate"));
                car.setBrand(resultSet.getString("brand"));
                car.setModel(resultSet.getString("model"));
                car.setYear(resultSet.getInt("year"));
                car.setFuelType(FuelType.valueOf(resultSet.getString("fuel_type")));
                car.setVehicleId(resultSet.getInt("vehicle_id"));
                car.setConcessionaireId(resultSet.getInt("id_concessionaire"));

                cars.add(car);  // Añadir el coche a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;  // Retornar la lista con los coches del concesionario
    }

}
