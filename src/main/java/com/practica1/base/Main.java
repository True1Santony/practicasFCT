package com.practica1.base;

import com.practica1.DAO.CarDAO;
import com.practica1.DAO.ConcessionaireDAO;
import com.practica1.DAO.MotorcycleDAO;
import com.practica1.model.Car;
import com.practica1.model.Concessionaire;
import com.practica1.model.Motorcycle;
import com.practica1.model.common.FuelType;

public class Main {

      static CarDAO carService = new CarDAO();
      static ConcessionaireDAO concessionaireService = new ConcessionaireDAO();
      static MotorcycleDAO motorcycleService = new MotorcycleDAO();

    public static void main(String[] args)  {

        DatabaseConnection.initializeDatabase();

        int concessionaireId1 = concessionaireService.create(new Concessionaire("KIA SA"));
        int concessionaireId2 = concessionaireService.create(new Concessionaire("BMW Logroño SAU"));

        carService.create(new Car("Honda","Civic",2005, FuelType.DIESEL,"5704GPO",5));
        carService.create(new Car("Seat","Azteca",2020, FuelType.DIESEL,"1456ASD",5));
        carService.create(new Car("Mazda","MX2",2005, FuelType.DIESEL,"5704GPN",5));

        motorcycleService.create(new Motorcycle("yamaha","amc", 2018, FuelType.GASOLINE, "4654ASD",600, concessionaireId1));
        motorcycleService.create(new Motorcycle("yamaha","amc", 2018, FuelType.GASOLINE, "5487ASD",600, concessionaireId1));
        motorcycleService.create(new Motorcycle("yamaha","amc", 2018, FuelType.GASOLINE, "54523SD",600, concessionaireId2));

       carService.findBylicensePlate("5704GPN")
               .get()
               .displayInformation();
    }
}