package com.pe.vet.veterinaria.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {
    private static final Logger LOGGER = Logger.getLogger(Conexion.class.getName());
    private static final String URL = "jdbc:mysql://Localhost:3307/db_veterinaria"; // Puerto 3307 y BD correcta
    private static final String USER = "root";
    private static final String PASSWORD = ""; // El que usas en MySQL

    public static Connection getConexion() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "No se pudo obtener la conexion a la base de datos.", e);
        }
        return con;
    }
}

