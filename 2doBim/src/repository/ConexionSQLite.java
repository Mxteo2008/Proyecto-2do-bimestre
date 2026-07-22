package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLite {

    private static final String URL = "jdbc:sqlite:2doBim.db";

    private ConexionSQLite() { }

    public static Connection obtenerConexion() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RepositorioException("No se encontro el driver JDBC de SQLite", e);
        }
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RepositorioException("No se pudo conectar a la base de datos SQLite", e);
        }
    }
}