package service;

import model.Activo;
import model.TipoActivo;
import repository.Reportable;
import repository.RepositorioException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReporteService {

    private final Reportable reportable;

    public ReporteService(Reportable reportable) {
        this.reportable = reportable;
    }

    public void imprimirCostoMantenimientoPorTipo() throws RepositorioException {
        System.out.println("=== Costo de mantenimiento por tipo de activo ===");
        Map<TipoActivo, Double> costos = new LinkedHashMap<>();
        for (TipoActivo tipo : TipoActivo.values()) {
            double costoTipo = reportable.filtrarPorTipo(tipo).stream()
                    .mapToDouble(Activo::calcularCostoMantenimiento)
                    .sum();
            costos.put(tipo, costoTipo);
        }
        costos.forEach((tipo, costo) -> System.out.printf("%-15s: %.2f%n", tipo, costo));
        System.out.printf("Total general    : %.2f%n", reportable.costoTotalMantenimiento());
    }

    public void imprimirCantidadActivosPorTipo() throws RepositorioException {
        System.out.println("=== Cantidad de activos por tipo ===");
        for (TipoActivo tipo : TipoActivo.values()) {
            List<Activo> activos = reportable.filtrarPorTipo(tipo);
            System.out.printf("%-15s: %d%n", tipo, activos.size());
        }
    }
}
