package com.practica1.controllers;

import com.practica1.model.Vehicle;
import com.practica1.service.dao.VehicleDao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private VehicleDao vehicleDaoSerice;

    public VehicleController(VehicleDao vehicleDaoService){
        this.vehicleDaoSerice = vehicleDaoService;
    }

    @GetMapping()
    public List<Vehicle> getAllVehicles(){
        return vehicleDaoSerice.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Vehicle>> getbyId(@PathVariable("id") int id) {
        return ResponseEntity.ok(vehicleDaoSerice.getById(id));
    }
}
