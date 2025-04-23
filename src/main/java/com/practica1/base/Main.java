package com.practica1.base;

import com.practica1.service.dao.*;
import com.practica1.model.Car;
import com.practica1.model.Concessionaire;
import com.practica1.model.Motorcycle;
import com.practica1.model.Vehicle;
import com.practica1.model.common.FuelType;

import java.io.File;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        deleteDatabaseFile();

        DatabaseConnection databaseConnection = new DatabaseConnectionImpl();
        databaseConnection.initializeDatabase();

        VehicleTypeRepository typeRepository = new VehicleTypeRepository(databaseConnection);

        CarDao carService = new CarDaoImpl(databaseConnection);
        MotorcycleDao motorcycleService = new MotorcycleDaoImpl(databaseConnection);
        VehicleDao vehicleService = new VehicleDaoImpl(carService, motorcycleService, typeRepository);
        ConcessionaireDao concessionaireService = new ConcessionaireDaoImpl(vehicleService, databaseConnection);

        //Insertar dos concesionarios.
        int concessionaireId1 = concessionaireService.insert(new Concessionaire("KIA SA"));
        int concessionaireId2 = concessionaireService.insert(new Concessionaire("BMW Logroño SAU"));

        //Insertar tres coches y tres motos, uno de ellos con matricula “5704GPN“, otro “5704GPO“, asociados a distintos concesionarios.
        vehicleService.insert(new Car("Honda","Civic",2005, FuelType.DIESEL,"5705GPA",5, concessionaireId2));
        vehicleService.insert(new Car("Seat","Azteca",2020, FuelType.DIESEL,"1456ASD",5, concessionaireId1));
        vehicleService.insert(new Car("Mazda","MX2",2005, FuelType.DIESEL,"5704GPN",5, concessionaireId2));
        vehicleService.insert(new Motorcycle("yamaha","amc", 2018, FuelType.GASOLINE, "4654ASD",600, concessionaireId1));
        vehicleService.insert(new Motorcycle("yamaha","amc", 2018, FuelType.GASOLINE, "5487ASD",600, concessionaireId1));
        vehicleService.insert(new Motorcycle("yamaha","amc", 2018, FuelType.GASOLINE, "5704GPO",600, concessionaireId2));

        //Buscar y mostrar información de un coche con matricula “5704GPN“, incluir la información del concesionario.
        vehicleService.findByLicensePlate("5704GPN").ifPresentOrElse(
                Vehicle::displayInformation,
                () -> System.out.println("No se encontró ningún vehículo con la matrícula: "));

       concessionaireService.infoConcessionareByLicencePlate("5704GPN");

       //Eliminar el vehículo por matricula “5704GPN“
       vehicleService.deleteByLicensePlate("5704GPN");

       //Buscar y mostrar mensaje de vehículo no encontrado con matricula “5704GPN“
        String licensePlateToSearch = "5704GPN";
        vehicleService.findByLicensePlate(licensePlateToSearch).ifPresentOrElse(
                Vehicle::displayInformation,
                () -> System.out.println("No se encontró ningún vehículo con la matrícula: " + licensePlateToSearch)
        );

        //Modificar algunos datos del vehículo con matricula “5704GPO“
        vehicleService.update("5704GPO", new Motorcycle("SEAT","MO", 2022, FuelType.ELECTRIC, "5704GPO",125, concessionaireId2));

        //Buscar todos los coches y mostrar su información.
        carService.findAll().forEach(Car::displayInformation);

        //Mostrar solo los coches de uno de los concesionarios.
        carService.findByConcessionaireId(concessionaireId2).forEach(Car::displayInformation);
    }

    private static void deleteDatabaseFile() {
        // Verificamos si el archivo existe
        File dbFile = new File("./concesionario_db.mv.db");

        if (!dbFile.exists()) {
            System.out.println("El archivo de la base de datos no existe.");
            return;
        }

        // Intentamos eliminar el archivo
        if (dbFile.delete()) {
            System.out.println("El archivo de la base de datos ha sido eliminado correctamente.");
        } else {
            System.out.println("Hubo un error al intentar eliminar el archivo de la base de datos.");
        }
    }
}