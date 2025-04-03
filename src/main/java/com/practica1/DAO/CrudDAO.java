package com.practica1.DAO;

import java.util.Optional;

public interface CrudDAO<T> {
    int create(T entity);
    Optional<T> findById(int id);
    void update(T entity);
    void deleteById(int id);
}
