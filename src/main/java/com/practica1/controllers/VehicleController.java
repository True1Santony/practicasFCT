package com.practica1.controllers;

import com.practica1.model.Vehicle;
import com.practica1.service.dao.VehicleDao;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleDao vehicleDaoSerice;

    @GetMapping()
    public List<Vehicle> getAllVehicles(){
        return vehicleDaoSerice.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Vehicle>> getbyId(@PathVariable("id") int id) {
        return ResponseEntity.ok(vehicleDaoSerice.getById(id));
    }

    @PostMapping("/filter")
    public List<Vehicle> filter(@RequestBody Vehicle filter) {
        return vehicleDaoSerice.filter(filter);
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertVehicle(@RequestBody Vehicle vehicle) {
        vehicleDaoSerice.insert(vehicle);
        return ResponseEntity.ok("Vehículo insertado correctamente");
    }

    @PutMapping("/update/{licensePlate}")
    public ResponseEntity<String> updateVehicle(@PathVariable("licensePlate") String licensePlate, @RequestBody Vehicle vehicle) {
        vehicleDaoSerice.update(licensePlate, vehicle);
        return ResponseEntity.ok("Vehículo actualizado correctamente");
    }

    @DeleteMapping("/delete/{licensePlate}")
    public ResponseEntity<String> deleteVehicle(@PathVariable("licensePlate") String licensePlate) {
        vehicleDaoSerice.deleteByLicensePlate(licensePlate);
        return ResponseEntity.ok("Vehículo eliminado correctamente");
    }

}
