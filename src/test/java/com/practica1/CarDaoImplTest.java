package com.practica1;

import com.practica1.base.DatabaseConnection;
import com.practica1.model.Car;
import com.practica1.model.common.FuelType;
import com.practica1.service.dao.CarDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CarDaoImplTest {

    private DatabaseConnection mockDb;
    private Connection mockConnection;
    private PreparedStatement mockStmt;
    private ResultSet mockRs;
    private CarDaoImpl carDao;

    @BeforeEach
    public void setUp() throws Exception {
        mockDb = mock(DatabaseConnection.class);
        mockConnection = mock(Connection.class);
        mockStmt = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);

        when(mockDb.getConnection()).thenReturn(mockConnection);
        carDao = new CarDaoImpl(mockDb);
    }

    @Test
    public void testCreate_Success() throws Exception {
        Car car = getTestCar();
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        int result = carDao.create(car, 1);

        assertEquals(1, result);
        verify(mockStmt).setInt(1, 4);
        verify(mockStmt).setString(2, "XYZ123");
    }

    @Test
    public void testUpdate_Success() throws Exception {
        Car car = getTestCar();
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        carDao.update(car, 10);

        verify(mockStmt).setInt(1, 4);
        verify(mockStmt).setString(2, "XYZ123");
        verify(mockStmt).setInt(8, 10);
    }

    @Test
    public void testDeleteById_Success() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        carDao.deleteById(10);

        verify(mockStmt).setInt(1, 10);
    }

    @Test
    public void testFindByLicensePlate_Found() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(true);
        mockResultSetWithCar(mockRs);

        Optional<Car> carOptional = carDao.findByLicensePlate("XYZ123");

        assertTrue(carOptional.isPresent());
        assertEquals("XYZ123", carOptional.get().getLicensePlate());
    }

    @Test
    public void testFindByLicensePlate_NotFound() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        Optional<Car> car = carDao.findByLicensePlate("ABC999");

        assertFalse(car.isPresent());
    }

    @Test
    public void testFindAll_ReturnsList() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(true, false); // Un resultado
        mockResultSetWithCar(mockRs);

        List<Car> cars = carDao.findAll();

        assertEquals(1, cars.size());
        assertEquals("XYZ123", cars.get(0).getLicensePlate());
    }

    @Test
    public void testFindByConcessionaireId_ReturnsList() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(true, false);
        mockResultSetWithCar(mockRs);

        List<Car> cars = carDao.findByConcessionaireId(1);

        assertEquals(1, cars.size());
        assertEquals(1, cars.get(0).getConcessionaireId());
    }

    // Helpers
    private Car getTestCar() {
        Car car = new Car();
        car.setNumberOfDoors(4);
        car.setLicensePlate("XYZ123");
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setYear(2020);
        car.setFuelType(FuelType.GASOLINE);
        car.setConcessionaireId(1);
        car.setVehicleId(1);
        return car;
    }

    private void mockResultSetWithCar(ResultSet rs) throws SQLException {
        when(rs.getInt("id")).thenReturn(10);
        when(rs.getInt("number_of_doors")).thenReturn(4);
        when(rs.getString("license_plate")).thenReturn("XYZ123");
        when(rs.getString("brand")).thenReturn("Toyota");
        when(rs.getString("model")).thenReturn("Corolla");
        when(rs.getInt("year")).thenReturn(2020);
        when(rs.getString("fuel_type")).thenReturn("GASOLINE");
        when(rs.getInt("vehicle_id")).thenReturn(1);
        when(rs.getInt("id_concessionaire")).thenReturn(1);
    }

}
