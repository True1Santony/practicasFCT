package com.practica1;

import com.practica1.base.DatabaseConnection;
import com.practica1.model.Car;
import com.practica1.model.common.FuelType;
import com.practica1.service.dao.CarDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
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

        verify(mockStmt).setObject(eq(1), eq(4), eq(Types.INTEGER));
        verify(mockStmt).setString(eq(2), eq("XYZ123"));
        verify(mockStmt).setObject(eq(3), eq("Toyota"), eq(Types.VARCHAR));
        verify(mockStmt).setObject(eq(4), eq("Corolla"), eq(Types.VARCHAR));
        verify(mockStmt).setObject(eq(5), eq(2020), eq(Types.INTEGER));
        verify(mockStmt).setObject(eq(6), eq("GASOLINE"), eq(Types.VARCHAR));
        verify(mockStmt).setInt(eq(7), eq(1));
        verify(mockStmt).setObject(eq(8), eq(1), eq(Types.INTEGER));

        verify(mockStmt).executeUpdate();
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

        when(mockRs.next()).thenReturn(true, false);
        mockResultSetWithCar(mockRs);

        List<Car> cars = carDao.findAll();

        assertEquals(1, cars.size());
        assertEquals("XYZ123", cars.getFirst().getLicensePlate());
    }

    @Test
    public void testFindByVehicleId_WhenCarsExist() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(true, false);
        mockResultSetWithCar(mockRs);

        // When
        Optional<List<Car>> result = carDao.findByVehicleId(100);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());

        Car car = result.get().getFirst();
        assertEquals(10, car.getId());
        assertEquals(4, car.getNumberOfDoors());
        assertEquals("XYZ123", car.getLicensePlate());
        assertEquals("Toyota", car.getBrand());
        assertEquals("Corolla", car.getModel());
        assertEquals(2020, car.getYear());
        assertEquals(FuelType.GASOLINE, car.getFuelType());
        assertEquals(1, car.getVehicleId());
        assertEquals(1, car.getConcessionaireId());

        verify(mockStmt).setInt(1, 100);
        verify(mockStmt).executeQuery();
    }

    @Test
    public void testFindByVehicleId_WhenNoCarsFound() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);


        Optional<List<Car>> result = carDao.findByVehicleId(999);

        // Verificación
        assertFalse(result.isPresent());
        verify(mockStmt).setInt(1, 999);
        verify(mockStmt).executeQuery();
    }

    @Test
    public void testFindByVehicleId_WhenSQLExceptionOccurs() throws SQLException {

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenThrow(new SQLException("Error de BD"));

        Optional<List<Car>> result = carDao.findByVehicleId(1);

        // Verificación
        assertFalse(result.isPresent());
        verify(mockStmt).setInt(1, 1);
        verify(mockStmt).executeQuery();
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
        when(rs.getObject("number_of_doors", Integer.class)).thenReturn(4);
        when(rs.getString("license_plate")).thenReturn("XYZ123");
        when(rs.getObject("brand", String.class)).thenReturn("Toyota");
        when(rs.getObject("model", String.class)).thenReturn("Corolla");
        when(rs.getObject("year", Integer.class)).thenReturn(2020);
        when(rs.getString("fuel_type")).thenReturn("GASOLINE");
        when(rs.getInt("vehicle_id")).thenReturn(1);
        when(rs.getObject("id_concessionaire", Integer.class)).thenReturn(1);
    }

}
