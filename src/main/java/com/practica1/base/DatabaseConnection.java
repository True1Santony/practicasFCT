package com.practica1.base;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnection {
        Connection getConnection() throws SQLException;
        void initializeDatabase();
    }

