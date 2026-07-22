/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.Activo;
import repository.RepositorioException;
import service.ActivoService;
/**
 *
 * @author Sexxxrvio
 */
public class ActivoController {    

    private final ActivoService activoService;

    public ActivoController(ActivoService activoService) {
        this.activoService = activoService;
    }

    public Activo crear(Activo activo) throws RepositorioException {
        return activoService.registrarActivo(activo);
    }

    public Optional<Activo> buscar(int id) throws RepositorioException {
        return activoService.obtenerActivo(id);
    }

    public List<Activo> listar() throws RepositorioException {
        return activoService.listarActivos();
    }

    public Activo actualizar(Activo activo) throws RepositorioException {
        return activoService.actualizarActivo(activo);
    }

    public boolean eliminar(int id) throws RepositorioException {
        return activoService.eliminarActivo(id);
    }
}

