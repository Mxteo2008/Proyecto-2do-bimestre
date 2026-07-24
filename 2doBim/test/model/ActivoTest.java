package model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

/**
 * Pruebas unitarias sobre la logica polimorfica de calculo de costo de
 * mantenimiento. No usan base de datos: son pruebas puras de modelo.
 */
public class ActivoTest {

    @Test
    public void hardwareNuevoCobraSoloElCargoFijo() {
        // Activo adquirido hoy: antiguedad = 0, factorDesgaste = 0
        Hardware hw = new Hardware(1, "Laptop Dell", LocalDate.now(), 1000.0, "ACTIVO", 5, "Dell");
        double esperado = 1000.0 * 0.10; // solo el 10% fijo, sin desgaste
        assertEquals(esperado, hw.calcularCostoMantenimiento(), 0.001);
    }

    @Test
    public void hardwareAlFinalDeSuVidaUtilCobraElMaximoDeDesgaste() {
        // Adquirido hace 10 anios, vida util 5 anios -> factorDesgaste se satura en 1.0
        LocalDate haceDiezAnios = LocalDate.now().minusYears(10);
        Hardware hw = new Hardware(2, "Servidor Viejo", haceDiezAnios, 2000.0, "ACTIVO", 5, "HP");
        double esperado = 2000.0 * 0.10 + 2000.0 * 0.15 * 1.0; // factor saturado en 1.0
        assertEquals(esperado, hw.calcularCostoMantenimiento(), 0.001);
    }

    @Test
    public void perifericoCobraTresPorcientoDelCostoBase() {
        Periferico mouse = new Periferico(3, "Mouse Logitech", LocalDate.now(), 50.0, "ACTIVO", "USB");
        assertEquals(50.0 * 0.03, mouse.calcularCostoMantenimiento(), 0.001);
    }

    @Test
    public void licenciaProximaAExpirarTieneRecargoDeUrgencia() {
        // Expira en 10 dias (<= 30) -> recargo del 20%
        LocalDate expiraPronto = LocalDate.now().plusDays(10);
        Licencia lic = new Licencia(4, "Office 365", LocalDate.now(), 300.0, "ACTIVO", expiraPronto, 3);
        double sinRecargo = (300.0 / 3) * 0.20 * 3; // = 60.0
        double esperado = sinRecargo * 1.20;
        assertEquals(esperado, lic.calcularCostoMantenimiento(), 0.001);
    }

    @Test
    public void licenciaLejosDeExpirarNoTieneRecargo() {
        LocalDate expiraLejos = LocalDate.now().plusYears(1);
        Licencia lic = new Licencia(5, "Antivirus", LocalDate.now(), 100.0, "ACTIVO", expiraLejos, 2);
        double esperado = (100.0 / 2) * 0.20 * 2; // sin recargo, factor 1.0
        assertEquals(esperado, lic.calcularCostoMantenimiento(), 0.001);
    }

    @Test
    public void dosActivosConElMismoIdSonIguales_LSP() {
       
        Activo a = new Hardware(7, "A", LocalDate.now(), 10, "ACTIVO", 1, "X");
        Activo b = new Periferico(7, "B", LocalDate.now(), 20, "ACTIVO", "USB");
        assertEquals(a, b);
    }

    @Test
    public void getTipoDevuelveElEnumCorrectoPorSubclase() {
        assertEquals(TipoActivo.HARDWARE, new Hardware(1, "x", LocalDate.now(), 1, "A", 1, "m").getTipo());
        assertEquals(TipoActivo.PERIFERICO, new Periferico(1, "x", LocalDate.now(), 1, "A", "USB").getTipo());
        assertEquals(TipoActivo.LICENCIA,
                new Licencia(1, "x", LocalDate.now(), 1, "A", LocalDate.now(), 1).getTipo());
    }
}
