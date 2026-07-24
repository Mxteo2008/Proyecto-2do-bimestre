package pkg2dobim;

import controller.ActivoController;
import controller.MantenimientoController;
import repository.ActivoRepository;
import repository.ActivoRepositorySQLite;
import repository.ConexionSQLite;
import repository.DatabaseInitializer;
import service.ActivoService;
import service.ConsolaView;
import service.ReporteService;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        Connection conexion = ConexionSQLite.obtenerConexion();
        DatabaseInitializer.inicializar(conexion);

        // Unico lugar del sistema donde se conoce la implementacion concreta (DIP):
        ActivoRepositorySQLite repositorioConcreto = new ActivoRepositorySQLite(conexion);
        ActivoRepository activoRepository = repositorioConcreto;      // se usa como interfaz CRUD
        // repositorioConcreto tambien implementa Reportable, se pasa directo abajo

        ActivoService activoService = new ActivoService(activoRepository);
        ReporteService reporteService = new ReporteService(repositorioConcreto);

        ActivoController activoController = new ActivoController(activoService);
        MantenimientoController mantenimientoController =
                new MantenimientoController(activoService, reporteService);

        ConsolaView consola = new ConsolaView(activoController, mantenimientoController);
        consola.iniciar();
    }
}
