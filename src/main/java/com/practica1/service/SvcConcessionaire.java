package com.practica1.service;

import com.practica1.model.Concessionaire;
import com.practica1.model.Vehicle;
import com.practica1.service.common.exception.DuplicateLicensePlateException;
import com.practica1.service.common.exception.EmptyLicensePlateException;
import com.practica1.service.common.exception.TypeNotFoundException;
import com.practica1.service.common.exception.VehicleNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SvcConcessionaire {

    private Concessionaire concessionaire;

    public SvcConcessionaire() {
        this.concessionaire = new Concessionaire();
    }

    //addVehiculo: Si la matrícula ya está en el HashMap, lanzar excepción
    public void addVehicle(Vehicle vehicle) throws DuplicateLicensePlateException {
        if (concessionaire.getVehiclesByLicensePlate().containsKey(vehicle.getLicensePlate())) {
            throw new DuplicateLicensePlateException(vehicle.getLicensePlate());
        }
        concessionaire.getVehicles().add(vehicle);
        concessionaire.getUniqueVehicleBrands().add(vehicle.getBrand());
        concessionaire.getVehiclesByLicensePlate().put(vehicle.getLicensePlate(), vehicle);
    }

    //findVehicleByLicensePlate: Si no existe, lanzar excepción personalizada
    public Vehicle findVehicleByLicensePlate(String licensePlate) throws VehicleNotFoundException {
        Vehicle vehicle = concessionaire.getVehiclesByLicensePlate().get(licensePlate);
        if (vehicle == null) {
            throw new VehicleNotFoundException(licensePlate);
        }
        return vehicle;
    }

    //getAllLicensePlates: si no hay ninguno lanzar excepción personalizada
    public Set<String> getAllLicensePlates() throws EmptyLicensePlateException {
        if (concessionaire.getVehiclesByLicensePlate().isEmpty()) {
            throw new EmptyLicensePlateException();
        }
        return concessionaire.getVehiclesByLicensePlate().keySet();
    }

    //filterByType: si no existe el tipo, lanzar excepción personalizada
    public List<Vehicle> filterByType(Class<?> type) throws TypeNotFoundException {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle: concessionaire.getVehicles()){
            if(type.isInstance(vehicle)){
                result.add(vehicle);
            }
        }
        if (result.isEmpty()) {
            throw new TypeNotFoundException(type.getSimpleName());
        }
        return result;
    }

    //deleteByLicensePlate: si no existe la matricula lanzar excepcion personalizada
    public void deleteByLicensePlate(String licensePlate) throws VehicleNotFoundException{
        if (!concessionaire.getVehiclesByLicensePlate().containsKey(licensePlate)) {
            throw new VehicleNotFoundException(licensePlate);
        }
        concessionaire.getVehicles().remove(concessionaire.getVehiclesByLicensePlate().get(licensePlate));
        concessionaire.getVehiclesByLicensePlate().remove(licensePlate);
    }

    public Concessionaire getConcessionaire() {
        return concessionaire;
    }
}
