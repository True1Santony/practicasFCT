package com.practica1.service.dao;

import com.practica1.base.DatabaseConnection;
import com.practica1.model.Car;
import com.practica1.model.common.FuelType;
import com.practica1.service.common.exception.DuplicateLicensePlateException;
import com.practica1.service.common.exception.EmptyLicensePlateException;
import com.practica1.service.common.exception.VehicleInsertionFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class CarDaoImpl implements CarDao{

    private final DatabaseConnection databaseConnection;

    public CarDaoImpl(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public int create(Car car, int vehicleId) {
        car.setVehicleId(vehicleId);
        String query = "INSERT INTO Car (number_of_doors, license_plate, brand, model, \"year\", fuel_type, vehicle_id, id_concessionaire) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query)) {
            stmt.setObject(1, car.getNumberOfDoors(), Types.INTEGER);
            stmt.setString(2, car.getLicensePlate());
            stmt.setObject(3, car.getBrand(), Types.VARCHAR);
            stmt.setObject(4, car.getModel(), Types.VARCHAR);
            stmt.setObject(5, car.getYear(), Types.INTEGER);
            stmt.setObject(6,
                    car.getFuelType() != null ? car.getFuelType().name() : null,
                    car.getFuelType() != null ? Types.VARCHAR : Types.NULL);
            stmt.setInt(7, car.getVehicleId());
            stmt.setObject(8, car.getConcessionaireId(), Types.INTEGER);

            stmt.executeUpdate();
            log.info("Coche insertado correctamente.");
        } catch (SQLException e) {
            if (e.getMessage().contains("license_plate")) {
                throw new DuplicateLicensePlateException(car.getLicensePlate());
            } else {
                throw new VehicleInsertionFailedException("Error al insertar el vehículo: " + e.getMessage());
            }
        }
        return 1;
    }

    public void update(Car car, int carId) {
        String query = "UPDATE Car SET number_of_doors = ?, license_plate = ?, brand = ?, model = ?, \"year\" = ?, fuel_type = ?, vehicle_id = ? WHERE id = ?";

        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query)) {

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
                log.info("Coche actualizado correctamente.");
            } else {
                log.error("Coche con el ID " + car.getId() + ", no encontrado.");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("license_plate")) {
                throw new EmptyLicensePlateException();
            } else {
                e.printStackTrace();
            }
        }
    }

    public void deleteById(int id) {
        String query = "DELETE FROM Car WHERE id = ?";

        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                log.info("Coche con el ID " + id + ", borrado correctamente.");
            } else {
                log.error("Coche con el ID " + id + ", no encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Car> findByLicensePlate(String licensePlate) {
        String query = "SELECT * FROM Car WHERE license_plate = ?";
        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query)) {

            stmt.setString(1, licensePlate);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt("id"));
                car.setNumberOfDoors(resultSet.getObject("number_of_doors", Integer.class));
                car.setLicensePlate(resultSet.getString("license_plate"));
                car.setBrand(resultSet.getObject("brand", String.class));
                car.setModel(resultSet.getObject("model", String.class));
                car.setYear(resultSet.getObject("year", Integer.class));

                String fuelTypeStr = resultSet.getString("fuel_type");
                if (fuelTypeStr != null) {
                    car.setFuelType(FuelType.valueOf(fuelTypeStr));
                } else {
                    car.setFuelType(null);
                }

                car.setVehicleId(resultSet.getObject("vehicle_id", Integer.class));
                car.setConcessionaireId(resultSet.getObject("id_concessionaire", Integer.class));

                return Optional.of(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM Car";

        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query)) {

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt("id"));
                car.setNumberOfDoors(resultSet.getObject("number_of_doors", Integer.class));
                car.setLicensePlate(resultSet.getString("license_plate"));
                car.setBrand(resultSet.getObject("brand", String.class));
                car.setModel(resultSet.getObject("model", String.class));
                car.setYear(resultSet.getObject("year", Integer.class));

                String fuelTypeStr = resultSet.getString("fuel_type");
                if (fuelTypeStr != null) {
                    car.setFuelType(FuelType.valueOf(fuelTypeStr));
                } else {
                    car.setFuelType(null);
                }
                car.setVehicleId(resultSet.getInt("vehicle_id"));
                car.setConcessionaireId(resultSet.getObject("id_concessionaire", Integer.class));

                cars.add(car);  // Añadir el coche a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;  // Retornar la lista con todos los coches
    }


    @Override
    public Optional<List<Car>> findByVehicleId(int id) {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM Car WHERE vehicle_id = ?";

        try (PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt("id"));
                car.setNumberOfDoors(resultSet.getObject("number_of_doors", Integer.class));
                car.setLicensePlate(resultSet.getString("license_plate"));
                car.setBrand(resultSet.getObject("brand", String.class));
                car.setModel(resultSet.getObject("model", String.class));
                car.setYear(resultSet.getObject("year", Integer.class));

                String fuelTypeStr = resultSet.getString("fuel_type");
                car.setFuelType(fuelTypeStr != null ? FuelType.valueOf(fuelTypeStr) : null);

                car.setVehicleId(resultSet.getInt("vehicle_id"));
                car.setConcessionaireId(resultSet.getObject("id_concessionaire", Integer.class));

                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(cars.isEmpty() ? null : cars);
    }

}
