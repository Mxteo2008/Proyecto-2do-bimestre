package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Licencia extends Activo {

    private LocalDate fechaExpiracion;
    private int numeroLicencias;

    public Licencia(int id, String nombre, LocalDate fechaAdquisicion, double costoBase,
                     String estado, LocalDate fechaExpiracion, int numeroLicencias) {
        super(id, nombre, fechaAdquisicion, costoBase, estado);
        this.fechaExpiracion = fechaExpiracion;
        this.numeroLicencias = numeroLicencias;
    }

    @Override
    public double calcularCostoMantenimiento() {
        long diasParaExpirar = fechaExpiracion != null
                ? ChronoUnit.DAYS.between(LocalDate.now(), fechaExpiracion)
                : 365;
        double recargoUrgencia = diasParaExpirar <= 30 ? 1.20 : 1.0;
        int licencias = Math.max(1, numeroLicencias);
        return (costoBase / licencias) * 0.20 * licencias * recargoUrgencia;
    }

    @Override
    public TipoActivo getTipo() { return TipoActivo.LICENCIA; }

    public LocalDate getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDate fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    public int getNumeroLicencias() { return numeroLicencias; }
    public void setNumeroLicencias(int numeroLicencias) { this.numeroLicencias = numeroLicencias; }
}