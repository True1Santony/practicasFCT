package com.practica1;

import com.practica1.model.Car;
import com.practica1.model.Motorcycle;

import com.practica1.model.Vehicle;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleDaoTest {

    @Mock
    private CarDao carService;

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
       // MockitoAnnotations.openMocks(this);

        try {
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    void testInsert_Car() throws Exception {
        // Arrange
        Car car = new Car("Honda", "Civic", 2005, FuelType.DIESEL, "5705GPA", 5, 20);
        car.setId(1);

        // Mock findIdByType usando when (no necesitas un espía)
        when(vehicleDao.findIdByType("CAR", connection)).thenReturn(Optional.of(1));

        // Mock carService.create
        when(carService.create(any(Car.class), eq(1), eq(connection))).thenReturn(1);

        // Act
        int result = vehicleDao.insert(car, connection);

        // Assert
        assertEquals(1, result); // Verificar que el resultado sea 1
        verify(carService).create(any(Car.class), eq(1), eq(connection)); // Verificar que se llamó a create con los argumentos correctos
    }
/*
    @Test
    void testInsert_Car() throws Exception {
        // Arrange
        Car car = new Car("Honda", "Civic", 2005, FuelType.DIESEL, "5705GPA", 5, 20);
        car.setId(1);

        //espía de VehicleDao
        VehicleDao vehicleDaoSpy = spy(new VehicleDao(carService, motorcycleService));

        // Mock findIdByType usando doReturn
        doReturn(Optional.of(1)).when(vehicleDaoSpy).findIdByType("CAR", connection);

        // Mock carService.create
        when(carService.create(any(Car.class), eq(1), eq(connection))).thenReturn(1);

        // Act
        int result = vehicleDaoSpy.insert(car, connection);

        // Assert
        assertEquals(1, result);
        verify(carService).create(any(Car.class), eq(1), eq(connection));
    }

    @Test
    void testInsert_Motorcycle() throws Exception {
        // Arrange
        Motorcycle motorcycle = new Motorcycle("yamaha", "amc", 2018, FuelType.GASOLINE, "5704GPO", 600, 1);

        // espía de VehicleDao
        VehicleDao vehicleDaoSpy = spy(new VehicleDao(carService, motorcycleService));

        // Mock findIdByType
        doReturn(Optional.of(1)).when(vehicleDaoSpy).findIdByType("MOTORCYCLE", connection);

        // Mocke motorcycleService.create
        when(motorcycleService.create(any(Motorcycle.class), eq(1), eq(connection))).thenReturn(1);

        // Act
        int result = vehicleDaoSpy.insert(motorcycle, connection);

        // Assert
        assertEquals(1, result);
        verify(motorcycleService).create(any(Motorcycle.class), eq(1), eq(connection));
    }


    @Test
    void testInsertAndCreateType_Success() throws Exception {
        // Arrange
        String vehicleType = "CAR";
        int generatedId = 1;

        // Configuramos el comportamiento del PreparedStatement
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1); // Simulamos que se ejecuta correctamente

        // Configuramos el comportamiento del ResultSet
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet); // Simulamos el ResultSet generado
        when(resultSet.next()).thenReturn(true); // Simulamos que hay un resultado
        when(resultSet.getInt(1)).thenReturn(generatedId); // Simulamos el ID generado

        // Act
        int result = vehicleDao.insertAndCreateType(vehicleType, connection);

        // Assert
        assertEquals(generatedId, result); // Verifica que el ID generado sea correcto
        verify(preparedStatement).setString(1, vehicleType); // Verifica que se estableció el tipo de vehículo
        verify(preparedStatement).executeUpdate(); // Verifica que se ejecutó la inserción
    }

    @Test
    void testUpdate_Car() throws Exception {
        // Arrange
        String licensePlate = "5705GPA";
        Car updatedCar = new Car("Honda", "Civic", 2005, FuelType.DIESEL, licensePlate, 5, 1);

        // Mock carService.findBylicensePlate para devolver un Optional con un coche existente
        Car existingCar = new Car("Honda", "Civic", 2005, FuelType.DIESEL, licensePlate, 5, 42);
        when(carService.findBylicensePlate(eq(licensePlate), eq(connection))).thenReturn(Optional.of(existingCar));

        // Act
        vehicleDao.update(licensePlate, updatedCar, connection);

        // Assert
        verify(carService).update(
                eq(updatedCar), // Verificaque el coche actualizado sea el mismo
                eq(existingCar.getId()), // Verifica que se use el ID del coche existente (42)
                eq(connection) // Verifica que se pase la conexión correcta
        );
    }

    @Test
    void testUpdate_Motorcycle() throws Exception {
        // Arrange
        String licensePlate = "5704GPO";
        Motorcycle updatedMotorcycle = new Motorcycle("Yamaha", "AMC", 2018, FuelType.GASOLINE, licensePlate, 600, 1);

        // Mock motorcycleService.findBylicensePlate para devolver un Optional con una moto
        Motorcycle existMotorcycle = new Motorcycle("Yamaha", "AMC", 2018, FuelType.GASOLINE, licensePlate, 600, 5);
        when(motorcycleService.findBylicensePlate(eq(licensePlate), eq(connection))).thenReturn(Optional.of(new Motorcycle("Yamaha", "AMC", 2018, FuelType.GASOLINE, licensePlate, 600, 42)));

        // Act
        vehicleDao.update(licensePlate, updatedMotorcycle, connection);

        // Assert
        verify(motorcycleService).update(
                eq(updatedMotorcycle),
                eq(existMotorcycle.getId()),
                eq(connection));
    }

    @Test
    void testUpdate_NoVehicleFound() throws Exception {
        // Arrange
        String licensePlate = "INVALID_LP";
        Car car = new Car("Honda", "Civic", 2005, FuelType.DIESEL, licensePlate, 5, 1);

        // Mock findBylicensePlate para ambos servicios para que no encuentren ningún vehículo
        when(carService.findBylicensePlate(eq(licensePlate), any(Connection.class))).thenReturn(Optional.empty());
        when(motorcycleService.findBylicensePlate(eq(licensePlate), any(Connection.class))).thenReturn(Optional.empty());

        // Act
        vehicleDao.update(licensePlate, car, connection);

        // Assert
        verify(carService, never()).update(any(Car.class), anyInt(), any(Connection.class)); // Verificamos que no se llamó al método update de carService
        verify(motorcycleService, never()).update(any(Motorcycle.class), anyInt(), any(Connection.class)); // Verificamos que no se llamó al método update de motorcycleService
    }

    @Test
    void testFindIdByType_ExistingVehicleType() throws Exception {
        // Arrange
        String vehicleType = "CAR";
        int expectedId = 1;

        // Configurar el comportamiento del mock PreparedStatement y ResultSet
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true); // Simulamos que hay resultados
        when(resultSet.getInt("id")).thenReturn(expectedId);

        // Act
        Optional<Integer> result = vehicleDao.findIdByType(vehicleType, connection);

        // Assert
        assertEquals(Optional.of(expectedId), result); // Verificar que el resultado es correcto

        // Verificar que los métodos fueron llamados correctamente
        verify(preparedStatement).setString(1, vehicleType);
        verify(resultSet).next();
        verify(resultSet).getInt("id");
    }

    @Test
    void testFindIdByType_NonExistingVehicleType() throws Exception {
        // Arrange
        String vehicleType = "TRUCK";

        // Configurar el comportamiento del mock PreparedStatement y ResultSet
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Simulamos que no hay resultados

        // Act
        Optional<Integer> result = vehicleDao.findIdByType(vehicleType, connection);

        // Assert
        assertEquals(Optional.empty(), result); // Verificar que el resultado es un Optional vacío

        // Verificar que los métodos fueron llamados correctamente
        verify(preparedStatement).setString(1, vehicleType);
        verify(resultSet).next();
    }

    @Test
    void testDeleteByLicensePlate_CarFound() throws Exception {
        // Arrange
        String licensePlate = "ABC123";
        int carId = 1;

        // Mock carService.findBylicensePlate to return a Car with the correct ID
        Car mockCar = new Car("Honda", "Civic", 2005, FuelType.DIESEL, "ABC123", 5, 1);
        mockCar.setId(carId);
        when(carService.findBylicensePlate(eq(licensePlate), eq(connection))).thenReturn(Optional.of(mockCar));

        // Mock motorcycleService.findBylicensePlate to return no result
        when(motorcycleService.findBylicensePlate(eq(licensePlate), eq(connection))).thenReturn(Optional.empty());

        // Act
        vehicleDao.deleteByLicensePlate(licensePlate, connection);

        // Assert
        verify(carService).deleteById(eq(carId), eq(connection)); // Verify deleteById was called with the correct ID
        verify(motorcycleService, never()).deleteById(anyInt(), eq(connection)); // Verify motorcycleService was not called
    }

    @Test
    void testDeleteByLicensePlate_MotorcycleFound() throws Exception {
        // Arrange
        String licensePlate = "XYZ789";
        int motorcycleId = 2;

        // Mock carService.findBylicensePlate para devolver un Optional vacío
        when(carService.findBylicensePlate(eq(licensePlate), eq(connection))).thenReturn(Optional.empty());

        // Mock motorcycleService.findBylicensePlate para devolver un Optional con una Motorcycle
        Motorcycle mockMotorcycle = new Motorcycle("Yamaha", "R1", 2018, FuelType.GASOLINE, "XYZ789", 600, motorcycleId);
        mockMotorcycle.setId(motorcycleId);
        when(motorcycleService.findBylicensePlate(eq(licensePlate), eq(connection))).thenReturn(Optional.of(mockMotorcycle));

        // Act
        vehicleDao.deleteByLicensePlate(licensePlate, connection);

        // Assert
        verify(carService, never()).deleteById(anyInt(), eq(connection)); // Verificar que no se llamó al carService
        verify(motorcycleService).deleteById(eq(motorcycleId), eq(connection)); // Verificar que se llamó a deleteById del motorcycleService
    }

    @Test
    void testDeleteByLicensePlate_NoVehicleFound() throws Exception {
        // Arrange
        String licensePlate = "NOPLATE";

        // Mock ambos servicios para devolver Optionals vacíos
        when(carService.findBylicensePlate(eq(licensePlate), eq(connection))).thenReturn(Optional.empty());
        when(motorcycleService.findBylicensePlate(eq(licensePlate), eq(connection))).thenReturn(Optional.empty());

        // Act
        vehicleDao.deleteByLicensePlate(licensePlate, connection);

        // Assert
        verify(carService, never()).deleteById(anyInt(), eq(connection)); // Verificar que no se llamó al carService
        verify(motorcycleService, never()).deleteById(anyInt(), eq(connection)); // Verificar que no se llamó al motorcycleService
    }

    @Test
    void testFindByLicensePlate_CarFound() throws Exception {
        // Arrange
        String licensePlate = "ABC123";
        int carId = 1;

        // Mock carService.findBylicensePlate para devolver un Optional con un Car
        Car mockCar = new Car("Honda", "Civic", 2005, FuelType.DIESEL, "ABC123", 5, 1);
        mockCar.setId(carId);
        when(carService.findBylicensePlate(eq(licensePlate), eq(connection))).thenReturn(Optional.of(mockCar));

        // Mock motorcycleService.findBylicensePlate para devolver un Optional vacío
        when(motorcycleService.findBylicensePlate(eq(licensePlate), eq(connection))).thenReturn(Optional.empty());

        // Act
        Optional<Vehicle> result = vehicleDao.findByLicensePlate(licensePlate, connection);

        // Assert
        assertTrue(result.isPresent()); // Verificar que hay un resultado
        assertEquals(mockCar, result.get()); // Verificar que el resultado es el Car esperado
    }

    @Test
    void testFindByLicensePlate_MotorcycleFound() throws Exception {
        // Arrange
        String licensePlate = "XYZ789";
        int motorcycleId = 2;

        // Mock carService.findBylicensePlate para devolver un Optional vacío
        when(carService.findBylicensePlate(eq(licensePlate), eq(connection))).thenReturn(Optional.empty());

        // Mock motorcycleService.findBylicensePlate para devolver un Optional con una Motorcycle
        Motorcycle mockMotorcycle = new Motorcycle("Yamaha", "R1", 2018, FuelType.GASOLINE, "XYZ789", 600, motorcycleId);
        mockMotorcycle.setId(motorcycleId);
        when(motorcycleService.findBylicensePlate(eq(licensePlate), eq(connection))).thenReturn(Optional.of(mockMotorcycle));

        // Act
        Optional<Vehicle> result = vehicleDao.findByLicensePlate(licensePlate, connection);

        // Assert
        assertTrue(result.isPresent()); // Verificar que hay un resultado
        assertEquals(mockMotorcycle, result.get()); // Verificar que el resultado es la Motorcycle esperada
    }

    @Test
    void testFindByLicensePlate_NoVehicleFound() throws Exception {
        // Arrange
        String licensePlate = "NOPLATE";

        // Mock ambos servicios para devolver Optionals vacíos
        when(carService.findBylicensePlate(eq(licensePlate), eq(connection))).thenReturn(Optional.empty());
        when(motorcycleService.findBylicensePlate(eq(licensePlate), eq(connection))).thenReturn(Optional.empty());

        // Act
        Optional<Vehicle> result = vehicleDao.findByLicensePlate(licensePlate, connection);

        // Assert
        assertFalse(result.isPresent()); // Verificar que no hay resultados
    }
*/
}
