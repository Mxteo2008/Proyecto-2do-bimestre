/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.Activo;
import repository.ActivoRepository;
import repository.RepositorioException;

import java.util.List;
import java.util.Optional;
/**
 *
 * @author Sexxxrvio
 */
public class ActivoService {

    private final ActivoRepository<Activo> activoRepository;

    // Inyeccion por constructor: el Service solo conoce la interfaz (DIP),
    // nunca sabe si detras hay SQLite, memoria u otra implementacion.
    public ActivoService(ActivoRepository<Activo> activoRepository) {
        this.activoRepository = activoRepository;
    }

    public Activo registrarActivo(Activo activo) throws RepositorioException {
        return activoRepository.guardar(activo);
    }

    public Optional<Activo> obtenerActivo(int id) throws RepositorioException {
        return activoRepository.buscarPorId(id);
    }

    public List<Activo> listarActivos() throws RepositorioException {
        return activoRepository.listarTodos();
    }

    public Activo actualizarActivo(Activo activo) throws RepositorioException {
        return activoRepository.actualizar(activo);
    }

    public boolean eliminarActivo(int id) throws RepositorioException {
        return activoRepository.eliminar(id);
    }

    // Logica polimorfica (LSP): no importa la subclase, cada Activo sabe calcularse a si mismo.
    // Si se agrega un nuevo tipo de Activo, este metodo no cambia (OCP).
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

