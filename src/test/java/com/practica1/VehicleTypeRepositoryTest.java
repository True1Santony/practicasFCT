package com.practica1;

import com.practica1.base.DatabaseConnection;
import com.practica1.service.dao.VehicleTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class VehicleTypeRepositoryTest {
    private DatabaseConnection dbMock;
    private Connection connMock;
    private PreparedStatement stmtMock;
    private ResultSet rsMock;

    private VehicleTypeRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        dbMock = mock(DatabaseConnection.class);
        connMock = mock(Connection.class);
        stmtMock = mock(PreparedStatement.class);
        rsMock = mock(ResultSet.class);

        when(dbMock.getConnection()).thenReturn(connMock);

        repository = new VehicleTypeRepository(dbMock);
    }

    @Test
    void testFindIdByType_found() throws Exception {
        // Arrange
        when(connMock.prepareStatement(anyString())).thenReturn(stmtMock);
        when(stmtMock.executeQuery()).thenReturn(rsMock);
        when(rsMock.next()).thenReturn(true);
        when(rsMock.getInt("id")).thenReturn(42);

        // Act
        Optional<Integer> result = repository.findIdByType("car");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
        verify(stmtMock).setString(1, "car");
    }

    @Test
    void testFindIdByType_notFound() throws Exception {
        // Arrange
        when(connMock.prepareStatement(anyString())).thenReturn(stmtMock);
        when(stmtMock.executeQuery()).thenReturn(rsMock);
        when(rsMock.next()).thenReturn(false);

        // Act
        Optional<Integer> result = repository.findIdByType("spaceship");

        // Assert
        assertFalse(result.isPresent());
        verify(stmtMock).setString(1, "spaceship");
    }

    @Test
    void testInsertAndCreateType_success() throws Exception {
        // Arrange
        when(connMock.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(stmtMock);
        when(stmtMock.getGeneratedKeys()).thenReturn(rsMock);
        when(rsMock.next()).thenReturn(true);
        when(rsMock.getInt(1)).thenReturn(7);

        // Act
        int generatedId = repository.insertAndCreateType("motorcycle");

        // Assert
        assertEquals(7, generatedId);
        verify(stmtMock).setString(1, "motorcycle");
        verify(stmtMock).executeUpdate();
    }

    @Test
    void testInsertAndCreateType_failure() throws Exception {
        // Arrange
        when(connMock.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(stmtMock);
        when(stmtMock.getGeneratedKeys()).thenReturn(rsMock);
        when(rsMock.next()).thenReturn(false); // Simula que no hay clave generada

        // Act
        int generatedId = repository.insertAndCreateType("truck");

        // Assert
        assertEquals(-1, generatedId);
    }
}
