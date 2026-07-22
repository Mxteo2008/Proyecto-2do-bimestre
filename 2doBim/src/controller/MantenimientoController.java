/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import repository.RepositorioException;
import service.ActivoService;
import service.ReporteService;
/**
 *
 * @author Sexxxrvio
 */
public class MantenimientoController {

    private final ActivoService activoService;
    private final ReporteService reporteService;

    public MantenimientoController(ActivoService activoService, ReporteService reporteService) {
        this.activoService = activoService;
        this.reporteService = reporteService;
    }

    public double costoMantenimientoTotal() throws RepositorioException {
        return activoService.calcularCostoMantenimientoTotal();
    }

    public double costoMantenimientoDeActivo(int id) throws RepositorioException {
        return activoService.calcularCostoMantenimientoPorActivo(id);
    }

    public void mostrarReportePorTipo() throws RepositorioException {
        reporteService.imprimirCostoMantenimientoPorTipo();
        reporteService.imprimirCantidadActivosPorTipo();
    }
}
