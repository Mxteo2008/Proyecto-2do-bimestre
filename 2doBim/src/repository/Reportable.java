package repository;

import model.Activo;
import model.TipoActivo;
import java.util.List;

/**
 * Interfaz chica y separada de reportes (ISP).
 */
public interface Reportable {

    List<Activo> filtrarPorTipo(TipoActivo tipo);

    double costoTotalMantenimiento();
}
