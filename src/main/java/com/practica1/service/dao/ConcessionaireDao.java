package com.practica1.service.dao;

import com.practica1.model.Concessionaire;

import java.sql.Connection;

public interface ConcessionaireDao {
    int insert(Concessionaire concessionaire);
    Concessionaire findById(int id);
    void infoConcessionareByLicencePlate(String licencePlate);
}
