package repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS activos (\n" + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" + "    tipo TEXT NOT NULL,\n" + "    nombre TEXT NOT NULL,\n" + "    fecha_adquisicion TEXT,\n" + "    costo_base REAL NOT NULL,\n" + "    estado TEXT,\n" + "    vida_util_anios INTEGER,\n" + "    marca TEXT,\n" + "    tipo_conexion TEXT,\n" + "    fecha_expiracion TEXT,\n" + "    numero_licencias INTEGER\n" + ")\n";

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
