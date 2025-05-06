package com.practica1;

import com.practica1.base.DatabaseConnection;
import com.practica1.model.Motorcycle;
import com.practica1.model.common.FuelType;
import com.practica1.service.dao.MotorcycleDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MotorcycleDaoImplTest {

    private DatabaseConnection mockDb;
    private Connection mockConnection;
    private PreparedStatement mockStmt;
    private ResultSet mockRs;
    private MotorcycleDaoImpl motorcycleDao;

    @BeforeEach
    public void setUp() throws Exception {
        mockDb = mock(DatabaseConnection.class);
        mockConnection = mock(Connection.class);
        mockStmt = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);

        when(mockDb.getConnection()).thenReturn(mockConnection);
        motorcycleDao = new MotorcycleDaoImpl(mockDb);
    }

    @Test
    public void testCreate_Success() throws Exception {
        Motorcycle moto = getTestMotorcycle();
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        int result = motorcycleDao.create(moto, 1);

        assertEquals(-1, result);

        verify(mockStmt).setObject(eq(1), eq(moto.getConcessionaireId()), eq(Types.INTEGER));
        verify(mockStmt).setObject(eq(2), eq(moto.getEngineDisplacement()), eq(Types.INTEGER));
        verify(mockStmt).setString(eq(3), eq(moto.getLicensePlate()));
        verify(mockStmt).setObject(eq(4), eq(moto.getBrand()), eq(Types.VARCHAR));
        verify(mockStmt).setObject(eq(5), eq(moto.getModel()), eq(Types.VARCHAR));
        verify(mockStmt).setObject(eq(6), eq(moto.getYear()), eq(Types.INTEGER));

        if (moto.getFuelType() != null) {
            verify(mockStmt).setObject(eq(7), eq(moto.getFuelType().name()), eq(Types.VARCHAR));
        } else {
            verify(mockStmt).setObject(eq(7), isNull(), eq(Types.NULL));
        }

        verify(mockStmt).setInt(eq(8), eq(1));

        verify(mockStmt).executeUpdate();
    }

    @Test
    public void testUpdate_Success() throws Exception {
        Motorcycle moto = getTestMotorcycle();
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        motorcycleDao.update(moto, 10);

        verify(mockStmt).setInt(1, moto.getEngineDisplacement());
        verify(mockStmt).setString(2, moto.getLicensePlate());
        verify(mockStmt).setString(3, moto.getBrand());
        verify(mockStmt).setInt(7, 10); // ID de la moto a actualizar
    }

    @Test
    public void testDeleteById_Success() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        motorcycleDao.deleteById(5);

        verify(mockStmt).setInt(1, 5);
        verify(mockStmt).executeUpdate();
    }

    @Test
    public void testFindByLicensePlate_Found() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        mockResultSetWithMotorcycle(mockRs);

        Optional<Motorcycle> result = motorcycleDao.findByLicensePlate("XYZ123");

        assertTrue(result.isPresent());
        assertEquals("XYZ123", result.get().getLicensePlate());
        assertEquals(125, result.get().getEngineDisplacement());
    }

    @Test
    public void testFindByLicensePlate_NotFound() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        Optional<Motorcycle> result = motorcycleDao.findByLicensePlate("ABC000");

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindAll_ReturnsListOfMotorcycles() throws SQLException {
        // Given
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);

        mockResultSetWithMotorcycle(mockRs);

        // When
        List<Motorcycle> result = motorcycleDao.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());

        Motorcycle moto = result.get(0);
        assertEquals(1, moto.getId());
        assertEquals(125, moto.getEngineDisplacement());
        assertEquals("XYZ123", moto.getLicensePlate());
        assertEquals("Yamaha", moto.getBrand());
        assertEquals("YZF-R125", moto.getModel());
        assertEquals(2021, moto.getYear());
        assertEquals(FuelType.GASOLINE, moto.getFuelType());
        assertEquals(1, moto.getVehicleId());
        assertEquals(1, moto.getConcessionaireId());

        verify(mockStmt).executeQuery();
    }

    @Test
    public void testFindByVehicleId_WhenMotorcycleExists() throws SQLException {
        // Given
        int vehicleId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false); // Una fila

        mockResultSetWithMotorcycle(mockRs);

        // When
        Optional<List<Motorcycle>> result = motorcycleDao.findByVehicleId(vehicleId);

        // Then
        assertTrue(result.isPresent());
        List<Motorcycle> motorcycles = result.get();
        assertEquals(1, motorcycles.size());

        Motorcycle moto = motorcycles.get(0);
        assertEquals(1, moto.getId());
        assertEquals(125, moto.getEngineDisplacement());
        assertEquals("XYZ123", moto.getLicensePlate());
        assertEquals("Yamaha", moto.getBrand());
        assertEquals("YZF-R125", moto.getModel());
        assertEquals(2021, moto.getYear());
        assertEquals(FuelType.GASOLINE, moto.getFuelType());
        assertEquals(1, moto.getVehicleId());
        assertEquals(1, moto.getConcessionaireId());

        verify(mockStmt).setInt(eq(1), eq(vehicleId));
        verify(mockStmt).executeQuery();
    }

    @Test
    public void testFindByVehicleId_WhenNoMotorcycleFound() throws SQLException {
        // Given
        int vehicleId = 999;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false); // No hay filas

        // When
        Optional<List<Motorcycle>> result = motorcycleDao.findByVehicleId(vehicleId);

        // Then
        assertFalse(result.isPresent());
        verify(mockStmt).setInt(eq(1), eq(vehicleId));
        verify(mockStmt).executeQuery();
    }

    @Test
    public void testFindByVehicleId_WhenSQLExceptionOccurs() throws SQLException {
        // Given
        int vehicleId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenThrow(new SQLException("Error de BD"));

        // When
        Optional<List<Motorcycle>> result = motorcycleDao.findByVehicleId(vehicleId);

        // Then
        assertFalse(result.isPresent());
        verify(mockStmt).setInt(eq(1), eq(vehicleId));
        verify(mockStmt).executeQuery();
    }

    // Helpers
    private Motorcycle getTestMotorcycle() {
        Motorcycle moto = new Motorcycle();
        moto.setId(1);
        moto.setEngineDisplacement(125);
        moto.setLicensePlate("XYZ123");
        moto.setBrand("Yamaha");
        moto.setModel("YZF-R125");
        moto.setYear(2021);
        moto.setFuelType(FuelType.GASOLINE);
        moto.setVehicleId(1);
        moto.setConcessionaireId(1);
        return moto;
    }

    private void mockResultSetWithMotorcycle(ResultSet rs) throws SQLException {
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getInt("engine_displacement")).thenReturn(125);
        when(rs.getString("license_plate")).thenReturn("XYZ123");
        when(rs.getString("brand")).thenReturn("Yamaha");
        when(rs.getString("model")).thenReturn("YZF-R125");
        when(rs.getInt("year")).thenReturn(2021);
        when(rs.getString("fuel_type")).thenReturn("GASOLINE");
        when(rs.getInt("vehicle_id")).thenReturn(1);
        when(rs.getInt("id_concessionaire")).thenReturn(1);
    }
}
