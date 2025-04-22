package com.practica1;

import com.practica1.base.DatabaseConnection;
import com.practica1.model.Car;
import com.practica1.model.Concessionaire;
import com.practica1.model.Motorcycle;
import com.practica1.service.dao.ConcessionaireDaoImpl;
import com.practica1.service.dao.VehicleDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ConcessionaireDaoImplTest {

        private DatabaseConnection dbMock;
        private Connection connMock;
        private PreparedStatement stmtMock;
        private ResultSet rsMock;
        private VehicleDao vehicleDaoMock;

        private ConcessionaireDaoImpl dao;

        @BeforeEach
        void setUp() throws Exception {
            dbMock = mock(DatabaseConnection.class);
            connMock = mock(Connection.class);
            stmtMock = mock(PreparedStatement.class);
            rsMock = mock(ResultSet.class);
            vehicleDaoMock = mock(VehicleDao.class);

            when(dbMock.getConnection()).thenReturn(connMock);

            dao = new ConcessionaireDaoImpl(vehicleDaoMock, dbMock);
        }

        @Test
        void testInsertConcessionaire() throws Exception {
            // Arrange
            Concessionaire c = new Concessionaire();
            c.setName("Concesionario Test");

            when(connMock.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(stmtMock);
            when(stmtMock.getGeneratedKeys()).thenReturn(rsMock);
            when(rsMock.next()).thenReturn(true);
            when(rsMock.getInt(1)).thenReturn(100);

            // Act
            int id = dao.insert(c);

            // Assert
            assertEquals(100, id);
            assertEquals(100, c.getId());
            verify(stmtMock).setString(1, "Concesionario Test");
            verify(stmtMock).executeUpdate();
        }

        @Test
        void testFindById() throws Exception {
            // Arrange
            when(connMock.prepareStatement(anyString())).thenReturn(stmtMock);
            when(stmtMock.executeQuery()).thenReturn(rsMock);
            when(rsMock.next()).thenReturn(true);
            when(rsMock.getInt("id")).thenReturn(200);
            when(rsMock.getString("name")).thenReturn("AutoCentro");

            // Act
            Concessionaire result = dao.findById(200);

            // Assert
            assertNotNull(result);
            assertEquals(200, result.getId());
            assertEquals("AutoCentro", result.getName());
            verify(stmtMock).setInt(1, 200);
        }

        @Test
        void testInfoConcessionareByLicencePlate_withCar() {
            // Arrange
            Car car = mock(Car.class);
            when(car.getConcessionaireId()).thenReturn(10);

            Concessionaire mockConcessionaire = mock(Concessionaire.class);

            when(vehicleDaoMock.findByLicensePlate("ABC123")).thenReturn(Optional.of(car));
            // Mockear findById con el concesionario
            ConcessionaireDaoImpl spyDao = spy(dao);
            doReturn(mockConcessionaire).when(spyDao).findById(10);

            // Act
            spyDao.infoConcessionareByLicencePlate("ABC123");

            // Assert
            verify(mockConcessionaire).displayInformation();
        }

        @Test
        void testInfoConcessionareByLicencePlate_withMotorcycle() {
            Motorcycle moto = mock(Motorcycle.class);
            when(moto.getConcessionaireId()).thenReturn(33);

            Concessionaire mockConcessionaire = mock(Concessionaire.class);
            when(vehicleDaoMock.findByLicensePlate("XYZ999")).thenReturn(Optional.of(moto));

            ConcessionaireDaoImpl spyDao = spy(dao);
            doReturn(mockConcessionaire).when(spyDao).findById(33);

            spyDao.infoConcessionareByLicencePlate("XYZ999");

            verify(mockConcessionaire).displayInformation();
        }
}

