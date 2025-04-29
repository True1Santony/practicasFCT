package com.practica1;

import com.practica1.model.Car;
import com.practica1.model.Motorcycle;
import com.practica1.model.Vehicle;
import com.practica1.model.common.FuelType;
import com.practica1.service.common.exception.VehicleNotFoundException;
import com.practica1.service.dao.CarDao;
import com.practica1.service.dao.MotorcycleDao;
import com.practica1.service.dao.VehicleDaoImpl;
import com.practica1.service.dao.VehicleTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Test vehículos 🚗 🏍️ 🛸")
public class VehicleDaoImplTest {

    @Mock
    private CarDao carService;

    @Mock
    private MotorcycleDao motorcycleService;

    @Mock
    private VehicleTypeRepository typeRepository;

    @InjectMocks
    private VehicleDaoImpl vehicleDao;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("🚗"+" inserta coche")
    void insertCar_WhenVehicleTypeExists_ShouldCallCarCreate() {
        // Arrange
        Car car = new Car();
        int vehicleId = 10;

        when(typeRepository.findIdByType("CAR")).thenReturn(Optional.of(vehicleId));
        when(carService.create(car, vehicleId)).thenReturn(1);

        // Act
        int result = vehicleDao.insert(car);

        // Assert
        verify(typeRepository).findIdByType("CAR");
        verify(carService).create(car, vehicleId);
        assertEquals(1, result);
    }

    @Test
    @DisplayName("🚗"+" inserta coche sin type")
    void insertCar_WhenVehicleTypeNotExists_ShouldInsertTypeAndCallCreate() {
        // Arrange
        Car car = new Car();
        int generatedId = 22;

        when(typeRepository.findIdByType("CAR")).thenReturn(Optional.empty());
        when(typeRepository.insertAndCreateType("CAR")).thenReturn(generatedId);
        when(carService.create(car, generatedId)).thenReturn(1);

        // Act
        int result = vehicleDao.insert(car);

        // Assert
        verify(typeRepository).insertAndCreateType("CAR");
        verify(carService).create(car, generatedId);
        assertEquals(1, result);
    }

    @Test
    @DisplayName("🏍️"+" inserta moto")
    void insertMotorcycle_WhenVehicleTypeExists_ShouldCallMotorcycleCreate() {
        // Arrange
        Motorcycle moto = new Motorcycle();
        int vehicleId = 5;

        when(typeRepository.findIdByType("MOTORCYCLE")).thenReturn(Optional.of(vehicleId));
        when(motorcycleService.create(moto, vehicleId)).thenReturn(1);

        // Act
        int result = vehicleDao.insert(moto);

        // Assert
        verify(typeRepository).findIdByType("MOTORCYCLE");
        verify(motorcycleService).create(moto, vehicleId);
        assertEquals(1, result);
    }

    @Test
    @DisplayName("🛸"+" inserta vehiculo desconocido")
    void insert_WithUnsupportedVehicleType_ShouldReturnMinusOne() {
        // Arrange
        Vehicle unknown = mock(Vehicle.class); // ni Car ni Motorcycle

        // Act
        int result = vehicleDao.insert(unknown);

        // Assert
        assertEquals(-1, result);
        verifyNoInteractions(typeRepository);
        verifyNoInteractions(carService);
        verifyNoInteractions(motorcycleService);
    }

    @Test
    @DisplayName("🚗"+" actualiza coche")
    void testUpdateCar() {
        // Arrange
        Car existingCar = new Car("Honda", "Civic", 2005, FuelType.DIESEL, "5705GPA", 5, 1);
        existingCar.setId(1);
        Car updatedCar = new Car("Honda", "Accord", 2020, FuelType.GASOLINE, "5705GPA", 5, 1);
        when(carService.findByLicensePlate("5705GPA")).thenReturn(Optional.of(existingCar));

        // Act
        vehicleDao.update("5705GPA", updatedCar);

        // Assert
        verify(carService, times(1)).update(updatedCar, existingCar.getId());
    }

    @Test
    @DisplayName("🏍️"+" actualiza moto")
    void testUpdateMotorcycle() {
        // Arrange
        Motorcycle existingMotorcycle = new Motorcycle("Yamaha", "AMC", 2018, null, "4654ASD", 600, 1);
        existingMotorcycle.setId(1);
        Motorcycle updatedMotorcycle = new Motorcycle("Yamaha", "R1", 2022, null, "4654ASD", 1000, 1);
        when(motorcycleService.findByLicensePlate("4654ASD")).thenReturn(Optional.of(existingMotorcycle));

        // Act
        vehicleDao.update("4654ASD", updatedMotorcycle);

        // Assert
        verify(motorcycleService, times(1)).update(updatedMotorcycle, existingMotorcycle.getId());
    }

    @Test
    @DisplayName("🚗 💩")
    void testDeleteByLicensePlateCar() {
        // Arrange
        Car car = new Car("Honda", "Civic", 2005, null, "5705GPA", 5, 1);
        car.setId(1);
        when(carService.findByLicensePlate("5705GPA")).thenReturn(Optional.of(car));

        // Act
        vehicleDao.deleteByLicensePlate("5705GPA");

        // Assert
        verify(carService, times(1)).deleteById(car.getId());
    }

    @Test
    @DisplayName("🏍️ 💩")
    void testDeleteByLicensePlateMotorcycle() {
        // Arrange
        Motorcycle motorcycle = new Motorcycle("Yamaha", "AMC", 2018, null, "4654ASD", 600, 1);
        motorcycle.setId(1);
        when(motorcycleService.findByLicensePlate("4654ASD")).thenReturn(Optional.of(motorcycle));

        // Act
        vehicleDao.deleteByLicensePlate("4654ASD");

        // Assert
        verify(motorcycleService, times(1)).deleteById(motorcycle.getId());
    }

    @Test
    @DisplayName("🔎 🚗 por matricula")
    void testFindByLicensePlateCar() {
        // Arrange
        Car car = new Car("Honda", "Civic", 2005, null, "5705GPA", 5, 1);
        when(carService.findByLicensePlate("5705GPA")).thenReturn(Optional.of(car));

        // Act
        Optional<Vehicle> result = vehicleDao.findByLicensePlate("5705GPA");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(car, result.get());
    }

    @Test
    @DisplayName("🔎 🏍️ por matricula")
    void testFindByLicensePlateMotorcycle() {
        // Arrange
        Motorcycle motorcycle = new Motorcycle("Yamaha", "AMC", 2018, null, "4654ASD", 600, 1);
        when(motorcycleService.findByLicensePlate("4654ASD")).thenReturn(Optional.of(motorcycle));

        // Act
        Optional<Vehicle> result = vehicleDao.findByLicensePlate("4654ASD");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(motorcycle, result.get());
    }

    @Test
    @DisplayName("🔎 🛸 por matricula")
    void testFindByLicensePlateNotFound() {
        // Arrange
        when(carService.findByLicensePlate("UNKNOWN")).thenReturn(Optional.empty());
        when(motorcycleService.findByLicensePlate("UNKNOWN")).thenReturn(Optional.empty());

        // Act
        Optional<Vehicle> result = vehicleDao.findByLicensePlate("UNKNOWN");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("🔎 Obtiene todos los vehículos (coches y motos)")
    void getAll_ShouldReturnAllVehicles() {
        // Arrange
        Car car = new Car();
        car.setBrand("Toyota");
        Motorcycle moto = new Motorcycle();
        moto.setBrand("Yamaha");

        when(carService.findAll()).thenReturn(List.of(car));
        when(motorcycleService.findAll()).thenReturn(List.of(moto));

        // Act
        List<Vehicle> vehicles = vehicleDao.getAll();

        // Assert
        assertEquals(2, vehicles.size());
        assertTrue(vehicles.contains(car));
        assertTrue(vehicles.contains(moto));
    }

    @Test
    @DisplayName("🔍 Busca vehículo por ID (coche encontrado)")
    void getById_WhenCarExists_ShouldReturnCar() throws VehicleNotFoundException {
        // Arrange
        int id = 1;
        Car car = new Car();
        car.setVehicleId(id);

        when(carService.findByVehicleId(id)).thenReturn(Optional.of(List.of(car)));

        // Act
        List<Vehicle> vehicles = vehicleDao.getById(id);

        // Assert
        assertEquals(1, vehicles.size());
        assertEquals(car, vehicles.get(0));
    }
    @Test
    @DisplayName("🔍 Busca vehículo por ID (moto encontrada)")
    void getById_WhenMotorcycleExists_ShouldReturnMotorcycle() throws VehicleNotFoundException {
        // Arrange
        int id = 2;
        Motorcycle moto = new Motorcycle();
        moto.setVehicleId(id);

        when(carService.findByVehicleId(id)).thenReturn(Optional.empty());
        when(motorcycleService.findByVehicleId(id)).thenReturn(Optional.of(List.of(moto)));

        // Act
        List<Vehicle> vehicles = vehicleDao.getById(id);

        // Assert
        assertEquals(1, vehicles.size());
        assertEquals(moto, vehicles.get(0));
    }

    @Test
    @DisplayName("❌ No encuentra vehículo por ID, lanza excepción")
    void getById_WhenNotFound_ShouldThrowException() {
        // Arrange
        int id = 999;

        when(carService.findByVehicleId(id)).thenReturn(Optional.empty());
        when(motorcycleService.findByVehicleId(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VehicleNotFoundException.class, () -> vehicleDao.getById(id));
    }

    @Test
    @DisplayName("🔍 Filtra vehículos (coches por marca)")
    void filter_ShouldReturnMatchingCars() {
        // Arrange
        Car filter = new Car();
        filter.setBrand("Toyota");

        Car car1 = new Car();
        car1.setBrand("Toyota");

        Car car2 = new Car();
        car2.setBrand("Honda");

        when(carService.findAll()).thenReturn(List.of(car1, car2));
        when(motorcycleService.findAll()).thenReturn(List.of());

        // Act
        List<Vehicle> result = vehicleDao.filter(filter);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Toyota", ((Car) result.get(0)).getBrand());
    }



}
