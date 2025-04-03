package com.practica1.base;

public class Main {

   // static SvcConcessionaire concessionaireService = new SvcConcessionaire();

    public static void main(String[] args)  {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        databaseConnection.initializeDatabase();

    }
/*
        Vehicle car = new Car("Seat", "Ibiza", 2000, FuelType.GASOLINE,"MA1234ZZ",5);

        executeMethods(car);

        Vehicle motorcycle = new Motorcycle("Honda", "CBR", 2005, FuelType.GASOLINE,"1234ABC", 600);

        executeMethods(motorcycle);

        List<Vehicle> vehicles = List.of(car,motorcycle);

        for (Vehicle vehicle1: vehicles){
            vehicle1.displayInformation();
        }

        System.out.println("*************Tarea PRC-12*******************");
        Vehicle motorcycle1 = new Motorcycle("Yamaha", "YBR", 2010, FuelType.GASOLINE,"4321CBA", 125);
        Vehicle motorcycle2 = new Motorcycle("Yamaha", "YBR", 2010, FuelType.GASOLINE,"4321CBA", 125);
        Vehicle car1 = new Car("Volkswage", "Golf", 2012, FuelType.DIESEL,"3884HQE",3);
        Vehicle car2 = new Car("Volkswage", "Golf", 2012, FuelType.DIESEL,"3884HQE",3);

        //En el main, usar los métodos para añadir vehiculos de distinto tipo.
        checkAndAddVehicle(car);
        checkAndAddVehicle(car1);
        checkAndAddVehicle(car2);
        checkAndAddVehicle(motorcycle);
        checkAndAddVehicle(motorcycle1);
        checkAndAddVehicle(motorcycle2);

        try {
            //Mostrar la informacion de los vehiculos utilizando los metodos implementados en el punto anterior.
            for (String licencePlate : concessionaireService.getAllLicensePlates()) {
                concessionaireService
                        .findVehicleByLicensePlate(licencePlate)
                        .displayInformation();
            }
        } catch (EmptyLicensePlateException | VehicleNotFoundException e) {
            System.out.println(e.getMessage());
        }

        //Usar TreeSet para ordenar por marca y volver a mostrar todos los vehiculos ordenados.
        System.out.println("********************************TreeSet por Marca de vehículo");

        Set<Vehicle> sortedVehicles = new TreeSet<>((v1, v2) -> v1.getBrand().compareTo(v2.getBrand()));
        sortedVehicles.addAll(concessionaireService.getConcessionaire().getVehicles());

        for (Vehicle vehicle : sortedVehicles) {
            vehicle.displayInformation();
        }
    }

    private static void executeMethods(Vehicle vehicle) {
        vehicle.accelerate();
        vehicle.brake();
        vehicle.displayInformation();
    }

    private static void checkAndAddVehicle(Vehicle vehicle){
        try {
            concessionaireService.addVehicle(vehicle);
        }catch (DuplicateLicensePlateException e){
            System.out.println(e.getMessage());
        }
    }
*/


}