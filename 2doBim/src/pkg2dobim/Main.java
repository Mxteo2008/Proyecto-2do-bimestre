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

        ActivoRepositorySQLite repositorioConcreto = new ActivoRepositorySQLite(conexion);
        ActivoRepository activoRepository = repositorioConcreto;      
        

        ActivoService activoService = new ActivoService(activoRepository);
        ReporteService reporteService = new ReporteService(repositorioConcreto);

        ActivoController activoController = new ActivoController(activoService);
        MantenimientoController mantenimientoController =
                new MantenimientoController(activoService, reporteService);

        ConsolaView consola = new ConsolaView(activoController, mantenimientoController);
        consola.iniciar();
    }
}
