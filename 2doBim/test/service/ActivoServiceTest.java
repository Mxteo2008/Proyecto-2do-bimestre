package service;


import repository.ActivoRepository;
import repository.RepositorioException;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Activo;
import model.Hardware;
import model.Periferico;

import static org.junit.Assert.*;

public class ActivoServiceTest {

    /**
     * Repositorio falso en memoria. Al implementar la interfaz ActivoRepository
     * (y no depender de la clase SQLite concreta), ActivoService puede probarse
     * sin base de datos real — esto es la ventaja practica de aplicar DIP.
     */
    public static class FakeActivoRepository implements ActivoRepository {
        public final List<Activo> datos = new ArrayList<>();
        public int siguienteId = 1;

        @Override
        public void guardar(Activo activo) {
            activo.setId(siguienteId++);
            datos.add(activo);
        }

        @Override
        public Optional<Activo> buscarPorId(int id) {
            return datos.stream().filter(a -> a.getId() == id).findFirst();
        }

        @Override
        public List<Activo> listarTodos() {
            return new ArrayList<>(datos);
        }

        @Override
        public void actualizar(Activo activo) {
            if (buscarPorId(activo.getId()).isEmpty()) {
                throw new RepositorioException("No existe activo con id " + activo.getId());
            }
        }

        @Override
        public void eliminar(int id) {
            if (!datos.removeIf(a -> a.getId() == id)) {
                throw new RepositorioException("No existe activo con id " + id);
            }
        }
    }

    private ActivoService service;

    @Before
    public void setUp() {
        service = new ActivoService(new FakeActivoRepository());
    }

    @Test
    public void registrarActivoLoAgregaYLeAsignaId() {
        Hardware hw = new Hardware(0, "PC", LocalDate.now(), 500, "ACTIVO", 3, "HP");
        Activo registrado = service.registrarActivo(hw);
        assertTrue(registrado.getId() > 0);
        assertEquals(1, service.listarActivos().size());
    }

    @Test
    public void eliminarActivoInexistenteDevuelveFalseSinLanzarExcepcion() {
        // eliminarActivo() atrapa la RepositorioException y devuelve false,
        // en vez de propagarla — se prueba ese comportamiento explicitamente.
        assertFalse(service.eliminarActivo(9999));
    }

    @Test
    public void calcularCostoMantenimientoTotalSumaTodosLosActivosRegistrados() {
        service.registrarActivo(new Periferico(0, "Mouse", LocalDate.now(), 100, "ACTIVO", "USB"));
        service.registrarActivo(new Periferico(0, "Teclado", LocalDate.now(), 200, "ACTIVO", "USB"));

        double esperado = 100 * 0.03 + 200 * 0.03;
        assertEquals(esperado, service.calcularCostoMantenimientoTotal(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void calcularCostoMantenimientoDeActivoInexistenteLanzaExcepcion() {
        service.calcularCostoMantenimientoPorActivo(9999);
    }
}
