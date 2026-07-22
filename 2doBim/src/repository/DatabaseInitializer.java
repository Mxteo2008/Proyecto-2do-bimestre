package repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS activos (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            tipo TEXT NOT NULL,
            nombre TEXT NOT NULL,
            fecha_adquisicion TEXT,
            costo_base REAL NOT NULL,
            estado TEXT,
            vida_util_anios INTEGER,
            marca TEXT,
            tipo_conexion TEXT,
            fecha_expiracion TEXT,
            numero_licencias INTEGER
        )
        """;

    private DatabaseInitializer() {
    }

    public static void inicializar(Connection conexion) {
        try (Statement stmt = conexion.createStatement()) {
            stmt.execute(CREATE_TABLE);
        } catch (SQLException e) {
            throw new RepositorioException("No se pudo inicializar el esquema", e);
        }
    }
}
