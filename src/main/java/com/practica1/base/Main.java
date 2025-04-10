package com.practica1.base;

import com.practica1.service.dao.CarDao;
import com.practica1.service.dao.ConcessionaireDao;
import com.practica1.service.dao.VehicleDao;
import com.practica1.model.Car;
import com.practica1.model.Concessionaire;
import com.practica1.model.Motorcycle;
import com.practica1.model.Vehicle;
import com.practica1.model.common.FuelType;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args)  {

        VehicleDao vehicleService = new VehicleDao();
        CarDao carService = new CarDao();
        ConcessionaireDao concessionaireService = new ConcessionaireDao();

        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.initializeDatabase();
        Connection connection;
        try {
           connection = databaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        //Insertar dos concesionarios.
        int concessionaireId1 = concessionaireService.insert(new Concessionaire("KIA SA"), connection);
        int concessionaireId2 = concessionaireService.insert(new Concessionaire("BMW Logroño SAU"), connection);

        //Insertar tres coches y tres motos, uno de ellos con matricula “5704GPN“, otro “5704GPO“, asociados a distintos concesionarios.
        vehicleService.insert(new Car("Honda","Civic",2005, FuelType.DIESEL,"5705GPA",5, concessionaireId2), connection);
        vehicleService.insert(new Car("Seat","Azteca",2020, FuelType.DIESEL,"1456ASD",5, concessionaireId1), connection);
        vehicleService.insert(new Car("Mazda","MX2",2005, FuelType.DIESEL,"5704GPN",5, concessionaireId2), connection);
        vehicleService.insert(new Motorcycle("yamaha","amc", 2018, FuelType.GASOLINE, "4654ASD",600, concessionaireId1), connection);
        vehicleService.insert(new Motorcycle("yamaha","amc", 2018, FuelType.GASOLINE, "5487ASD",600, concessionaireId1), connection);
        vehicleService.insert(new Motorcycle("yamaha","amc", 2018, FuelType.GASOLINE, "5704GPO",600, concessionaireId2), connection);

        //Buscar y mostrar información de un coche con matricula “5704GPN“, incluir la información del concesionario.
        vehicleService.findByLicensePlate("5704GPN", connection).ifPresentOrElse(
                Vehicle::displayInformation,
                () -> System.out.println("No se encontró ningún vehículo con la matrícula: "));

       concessionaireService.infoConcessionareByLicencePlate("5704GPN", connection);

       //Eliminar el vehículo por matricula “5704GPN“
       vehicleService.deleteByLicensePlate("5704GPN", connection);

       //Buscar y mostrar mensaje de vehículo no encontrado con matricula “5704GPN“
        String licensePlateToSearch = "5704GPN";
        vehicleService.findByLicensePlate(licensePlateToSearch, connection).ifPresentOrElse(
                Vehicle::displayInformation,
                () -> System.out.println("No se encontró ningún vehículo con la matrícula: " + licensePlateToSearch)
        );

        //Modificar algunos datos del vehículo con matricula “5704GPO“
        vehicleService.update("5704GPO", new Motorcycle("SEAT","MO", 2022, FuelType.ELECTRIC, "5704GPO",125, concessionaireId2), connection);

        //Buscar todos los coches y mostrar su información.
        carService.findAll(connection).forEach(Car::displayInformation);

        //Mostrar solo los coches de uno de los concesionarios.
        carService.findByConcessionaireId(concessionaireId2, connection).forEach(Car::displayInformation);

    }
}