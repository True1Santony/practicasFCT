package com.practica1;

import com.practica1.base.DatabaseConnection;
import com.practica1.model.Motorcycle;
import com.practica1.model.common.FuelType;
import com.practica1.service.dao.MotorcycleDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
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
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        int result = motorcycleDao.create(moto, 1);

        // El método siempre retorna -1 por diseño
        assertEquals(-1, result);
        verify(mockStmt).setInt(1, moto.getConcessionaireId());
        verify(mockStmt).setInt(2, moto.getEngineDisplacement());
        verify(mockStmt).setString(3, moto.getLicensePlate());
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
