package model;

import java.time.LocalDate;

public class Hardware extends Activo {

    private int vidaUtilAnios;
    private String marca;

    public Hardware(int id, String nombre, LocalDate fechaAdquisicion, double costoBase,
                     String estado, int vidaUtilAnios, String marca) {
        super(id, nombre, fechaAdquisicion, costoBase, estado);
        this.vidaUtilAnios = vidaUtilAnios;
        this.marca = marca;
    }

    @Override
    public double calcularCostoMantenimiento() {
        int antiguedad = antiguedadEnAnios();
        double factorDesgaste = vidaUtilAnios > 0
                ? Math.min(1.0, (double) antiguedad / vidaUtilAnios)
                : 0.5;
        return costoBase * 0.10 + costoBase * 0.15 * factorDesgaste;
    }

    @Override
    public TipoActivo getTipo() { return TipoActivo.HARDWARE; }

    public int getVidaUtilAnios() { return vidaUtilAnios; }
    public void setVidaUtilAnios(int vidaUtilAnios) { this.vidaUtilAnios = vidaUtilAnios; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
}