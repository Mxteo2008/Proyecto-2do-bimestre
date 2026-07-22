package model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Clase abstracta base de toda la jerarquia de activos (SRP: solo datos
 * y comportamiento propio del activo, nada de persistencia aqui).
 */
public abstract class Activo {

    protected int id;
    protected String nombre;
    protected LocalDate fechaAdquisicion;
    protected double costoBase;
    protected String estado;

    protected Activo(int id, String nombre, LocalDate fechaAdquisicion, double costoBase, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.fechaAdquisicion = fechaAdquisicion;
        this.costoBase = costoBase;
        this.estado = estado;
    }

    // Metodo abstracto: cada subclase calcula su propio mantenimiento (polimorfismo)
    public abstract double calcularCostoMantenimiento();

    public abstract TipoActivo getTipo();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaAdquisicion() { return fechaAdquisicion; }
    public void setFechaAdquisicion(LocalDate fechaAdquisicion) { this.fechaAdquisicion = fechaAdquisicion; }

    public double getCostoBase() { return costoBase; }
    public void setCostoBase(double costoBase) { this.costoBase = costoBase; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    protected int antiguedadEnAnios() {
        if (fechaAdquisicion == null) return 0;
        return LocalDate.now().getYear() - fechaAdquisicion.getYear();
    }

    @Override
    public String toString() {
        return String.format("[%d] %-10s %-20s costoBase=$%.2f estado=%s mantenimiento=$%.2f",
                id, getTipo(), nombre, costoBase, estado, calcularCostoMantenimiento());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activo)) return false;
        return id == ((Activo) o).id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}