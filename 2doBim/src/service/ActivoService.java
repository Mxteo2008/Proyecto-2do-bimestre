package service;

import model.Activo;
import repository.ActivoRepository;
import repository.RepositorioException;

import java.util.List;
import java.util.Optional;

public class ActivoService {

    private final ActivoRepository activoRepository;


    public ActivoService(ActivoRepository activoRepository) {
        this.activoRepository = activoRepository;
    }

    public Activo registrarActivo(Activo activo) throws RepositorioException {
        activoRepository.guardar(activo); // guardar() ya asigna el id generado dentro del propio objeto
        return activo;
    }

    public Optional<Activo> obtenerActivo(int id) throws RepositorioException {
        return activoRepository.buscarPorId(id);
    }

    public List<Activo> listarActivos() throws RepositorioException {
        return activoRepository.listarTodos();
    }

    public Activo actualizarActivo(Activo activo) throws RepositorioException {
        activoRepository.actualizar(activo);
        return activo;
    }

    public boolean eliminarActivo(int id) {
        try {
            activoRepository.eliminar(id);
            return true;
        } catch (RepositorioException e) {
            return false;
        }
    }

  
    public double calcularCostoMantenimientoTotal() throws RepositorioException {
        return activoRepository.listarTodos().stream()
                .mapToDouble(Activo::calcularCostoMantenimiento)
                .sum();
    }

    public double calcularCostoMantenimientoPorActivo(int id) throws RepositorioException {
        return obtenerActivo(id)
                .map(Activo::calcularCostoMantenimiento)
                .orElseThrow(() -> new IllegalArgumentException("Activo no encontrado con id: " + id));
    }
}
