package com.practica1.base;

import com.practica1.DAO.CarDAO;
import com.practica1.DAO.ConcessionaireDAO;
import com.practica1.DAO.VehicleDAO;
import com.practica1.model.Car;
import com.practica1.model.Concessionaire;
import com.practica1.model.Motorcycle;
import com.practica1.model.Vehicle;
import com.practica1.model.common.FuelType;

public class Main {

      static VehicleDAO vehicleService = new VehicleDAO();
      static CarDAO carService = new CarDAO();
      static ConcessionaireDAO concessionaireService = new ConcessionaireDAO();

    public static void main(String[] args)  {

        DatabaseConnection.initializeDatabase();

        //Insertar dos concesionarios.
        int concessionaireId1 = concessionaireService.create(new Concessionaire("KIA SA"));
        int concessionaireId2 = concessionaireService.create(new Concessionaire("BMW Logroño SAU"));

        //Insertar tres coches y tres motos, uno de ellos con matricula “5704GPN“, otro “5704GPO“, asociados a distintos concesionarios.
        vehicleService.create(new Car("Honda","Civic",2005, FuelType.DIESEL,"5705GPA",5, concessionaireId2));
        vehicleService.create(new Car("Seat","Azteca",2020, FuelType.DIESEL,"1456ASD",5, concessionaireId1));
        vehicleService.create(new Car("Mazda","MX2",2005, FuelType.DIESEL,"5704GPN",5, concessionaireId2));
        vehicleService.create(new Motorcycle("yamaha","amc", 2018, FuelType.GASOLINE, "4654ASD",600, concessionaireId1));
        vehicleService.create(new Motorcycle("yamaha","amc", 2018, FuelType.GASOLINE, "5487ASD",600, concessionaireId1));
        vehicleService.create(new Motorcycle("yamaha","amc", 2018, FuelType.GASOLINE, "5704GPO",600, concessionaireId2));

        //Buscar y mostrar información de un coche con matricula “5704GPN“, incluir la información del concesionario.
        vehicleService.findByLicensePlate("5704GPO").ifPresentOrElse(
                Vehicle::displayInformation,
                () -> System.out.println("No se encontró ningún vehículo con la matrícula: "));

       concessionaireService.infoConcessionareByLicencePlate("5704GPN");

       //Eliminar el vehículo por matricula “5704GPN“
       vehicleService.deleteByLicensePlate("5704GPN");

       //Buscar y mostrar mensaje de vehículo no encontrado con matricula “5704GPN“
        String licensePlateToSearch = "5704GPN";
        vehicleService.findByLicensePlate(licensePlateToSearch).ifPresentOrElse(
                vehicle -> vehicle.displayInformation(),
                () -> System.out.println("No se encontró ningún vehículo con la matrícula: " + licensePlateToSearch)
        );

        //Modificar algunos datos del vehículo con matricula “5704GPO“
        vehicleService.update("5704GPO", new Motorcycle("SEAT","MO", 2022, FuelType.ELECTRIC, "5704GPO",125, concessionaireId2));

        //Buscar todos los coches y mostrar su información.
        carService.findAll().forEach(Car::displayInformation);

        //Mostrar solo los coches de uno de los concesionarios.
        carService.findByConcessionaireId(concessionaireId2).forEach(Car::displayInformation);

    }
}