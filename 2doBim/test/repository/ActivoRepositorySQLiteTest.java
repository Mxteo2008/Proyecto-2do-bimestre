package repository;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import model.Hardware;
import model.Licencia;
import model.Periferico;
import model.TipoActivo;

import static org.junit.Assert.*;

/**
 * Pruebas de integracion del repositorio contra una base SQLite EN MEMORIA
 * (jdbc:sqlite::memory:), asi que nunca tocan el archivo 2doBim.db real.
 * Verifican el CRUD completo y los metodos de Reportable.
 */
public class ActivoRepositorySQLiteTest {

    public Connection conexion;
    public ActivoRepositorySQLite repositorio;

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        conexion = DriverManager.getConnection("jdbc:sqlite::memory:");
        DatabaseInitializer.inicializar(conexion);
        repositorio = new ActivoRepositorySQLite(conexion);
    }

    @After
    public void tearDown() throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }

    @Test
    public void guardarAsignaUnIdGeneradoAutomaticamente() {
        Hardware hw = new Hardware(0, "Laptop", LocalDate.now(), 800.0, "ACTIVO", 4, "Lenovo");
        repositorio.guardar(hw);
        assertTrue(hw.getId() > 0);
    }

    @Test
    public void buscarPorIdEncuentraElActivoGuardado() {
        Periferico p = new Periferico(0, "Teclado", LocalDate.now(), 40.0, "ACTIVO", "USB");
        repositorio.guardar(p);

        Optional<model.Activo> resultado = repositorio.buscarPorId(p.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Teclado", resultado.get().getNombre());
        assertEquals(TipoActivo.PERIFERICO, resultado.get().getTipo());
    }

    @Test
    public void buscarPorIdInexistenteDevuelveOptionalVacio() {
        assertFalse(repositorio.buscarPorId(9999).isPresent());
    }

    @Test
    public void listarTodosDevuelveTodosLosActivosGuardados() {
        repositorio.guardar(new Hardware(0, "PC 1", LocalDate.now(), 500, "ACTIVO", 3, "HP"));
        repositorio.guardar(new Periferico(0, "Mouse 1", LocalDate.now(), 20, "ACTIVO", "USB"));
        repositorio.guardar(new Licencia(0, "Windows", LocalDate.now(), 150, "ACTIVO", LocalDate.now().plusYears(1), 1));

        List<model.Activo> todos = repositorio.listarTodos();

        assertEquals(3, todos.size());
    }

    @Test
    public void actualizarModificaLosDatosDelActivo() {
        Hardware hw = new Hardware(0, "Monitor", LocalDate.now(), 300.0, "ACTIVO", 6, "Samsung");
        repositorio.guardar(hw);

        hw.setNombre("Monitor 24 pulgadas");
        hw.setCostoBase(350.0);
        repositorio.actualizar(hw);

        Optional<model.Activo> actualizado = repositorio.buscarPorId(hw.getId());
        assertTrue(actualizado.isPresent());
        assertEquals("Monitor 24 pulgadas", actualizado.get().getNombre());
        assertEquals(350.0, actualizado.get().getCostoBase(), 0.001);
    }

    @Test(expected = RepositorioException.class)
    public void actualizarUnActivoInexistenteLanzaExcepcion() {
        Hardware fantasma = new Hardware(9999, "No existe", LocalDate.now(), 1, "ACTIVO", 1, "X");
        repositorio.actualizar(fantasma);
    }

    @Test
    public void eliminarBorraElActivoDeLaBaseDeDatos() {
        Periferico p = new Periferico(0, "Webcam", LocalDate.now(), 60.0, "ACTIVO", "USB");
        repositorio.guardar(p);

        repositorio.eliminar(p.getId());

        assertFalse(repositorio.buscarPorId(p.getId()).isPresent());
    }

    @Test(expected = RepositorioException.class)
    public void eliminarUnIdInexistenteLanzaExcepcion() {
        repositorio.eliminar(9999);
    }

    @Test
    public void filtrarPorTipoDevuelveSoloLosActivosDeEseTipo() {
        repositorio.guardar(new Hardware(0, "PC", LocalDate.now(), 500, "ACTIVO", 3, "HP"));
        repositorio.guardar(new Hardware(0, "Laptop", LocalDate.now(), 700, "ACTIVO", 3, "Dell"));
        repositorio.guardar(new Periferico(0, "Mouse", LocalDate.now(), 20, "ACTIVO", "USB"));

        List<model.Activo> hardwares = repositorio.filtrarPorTipo(TipoActivo.HARDWARE);

        assertEquals(2, hardwares.size());
    }

    @Test
    public void costoTotalMantenimientoSumaElDeTodosLosActivos() {
        Periferico p1 = new Periferico(0, "Mouse", LocalDate.now(), 100.0, "ACTIVO", "USB");
        Periferico p2 = new Periferico(0, "Teclado", LocalDate.now(), 200.0, "ACTIVO", "USB");
        repositorio.guardar(p1);
        repositorio.guardar(p2);

        double esperado = p1.calcularCostoMantenimiento() + p2.calcularCostoMantenimiento();
        assertEquals(esperado, repositorio.costoTotalMantenimiento(), 0.001);
    }
}
