package com.practica1.base;

import com.practica1.DAO.CarDAO;
import com.practica1.DAO.ConcessionaireDAO;
import com.practica1.model.Car;
import com.practica1.model.Concessionaire;
import com.practica1.model.common.FuelType;

public class Main {

      static CarDAO carService = new CarDAO();
      static ConcessionaireDAO concessionaireService = new ConcessionaireDAO();

    public static void main(String[] args)  {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.initializeDatabase();

        concessionaireService.create(new Concessionaire("KIA SA"));
        concessionaireService.create(new Concessionaire("BMW Logroño SAU"));

        /*Car car = new Car("seat","leon",2005,FuelType.DIESEL,"1234ASD",5);
        car.setVehicleId(1);
        carService.create(car);
*/

    }
}