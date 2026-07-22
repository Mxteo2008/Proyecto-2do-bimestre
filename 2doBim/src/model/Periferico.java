package model;

import java.time.LocalDate;

public class Periferico extends Activo {

    private String tipoConexion;

    public Periferico(int id, String nombre, LocalDate fechaAdquisicion, double costoBase,
                       String estado, String tipoConexion) {
        super(id, nombre, fechaAdquisicion, costoBase, estado);
        this.tipoConexion = tipoConexion;
    }

    @Override
    public double calcularCostoMantenimiento() {
        return costoBase * 0.03;
    }

    @Override
    public TipoActivo getTipo() { return TipoActivo.PERIFERICO; }

    public String getTipoConexion() { return tipoConexion; }
    public void setTipoConexion(String tipoConexion) { this.tipoConexion = tipoConexion; }
}