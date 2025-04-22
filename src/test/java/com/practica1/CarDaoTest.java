package com.practica1;

import com.practica1.model.Car;
import com.practica1.model.common.FuelType;
import com.practica1.service.dao.CarDao;
import com.practica1.service.dao.MotorcycleDao;
import com.practica1.service.dao.VehicleDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


public class CarDaoTest {
    @Mock
    private CarDao carDao;

    @Mock
    private MotorcycleDao motorcycleService;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private VehicleDao vehicleDao;

    @Mock
    private VehicleDao vehicleDaoMock;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks antes de cada prueba
        MockitoAnnotations.openMocks(this);

        try {
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreate() throws Exception {
        // Arrange
        Car car = new Car("Honda", "Civic", 2005, FuelType.DIESEL, "5705GPA", 5, 1);
        int vehicleId = 20;

        // Mock the behavior of Connection and PreparedStatement
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        // Configurar los setters de PreparedStatement
        doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
        doNothing().when(preparedStatement).setString(anyInt(), anyString());

        // Configurar el comportamiento de executeUpdate()
        when(preparedStatement.executeUpdate()).thenReturn(1); // Simulamos que se insertó una fila

        // Act
        int result = carDao.create(car, vehicleId, connection);

        // Assert
        assertEquals(1, result); // Verificar que el resultado sea 1 (éxito)
        verify(preparedStatement).setInt(1, car.getNumberOfDoors());
        verify(preparedStatement).setString(2, car.getLicensePlate());
        verify(preparedStatement).setString(3, car.getBrand());
        verify(preparedStatement).setString(4, car.getModel());
        verify(preparedStatement).setInt(5, car.getYear());
        verify(preparedStatement).setString(6, car.getFuelType().name());
        verify(preparedStatement).setInt(7, vehicleId);
        verify(preparedStatement).setInt(8, car.getConcessionaireId());
        verify(preparedStatement).executeUpdate(); // Verificar que se llamó a executeUpdate()
    }

}
