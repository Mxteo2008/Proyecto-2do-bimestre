/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import repository.Reportable;
import repository.RepositorioException;

import java.util.Map;
/**
 *
 * @author Sexxxrvio
 */
public class ReporteService {

    private final Reportable reportable;

    // Depende solo de Reportable, no de todo ActivoRepository (ISP).
    public ReporteService(Reportable reportable) {
        this.reportable = reportable;
    }

    public void imprimirCostoMantenimientoPorTipo() throws RepositorioException {
        Map<String, Double> costos = reportable.costoMantenimientoPorTipo();
        System.out.println("=== Costo de mantenimiento por tipo de activo ===");
        costos.forEach((tipo, costo) -> System.out.printf("%-15s: %.2f%n", tipo, costo));
        System.out.printf("Total general    : %.2f%n",
                costos.values().stream().mapToDouble(Double::doubleValue).sum());
    }

    public void imprimirCantidadActivosPorTipo() throws RepositorioException {
        Map<String, Integer> cantidades = reportable.cantidadActivosPorTipo();
        System.out.println("=== Cantidad de activos por tipo ===");
        cantidades.forEach((tipo, cantidad) -> System.out.printf("%-15s: %d%n", tipo, cantidad));
    }
}