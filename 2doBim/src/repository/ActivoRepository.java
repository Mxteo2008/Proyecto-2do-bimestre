package repository;

import model.Activo;
import java.util.List;
import java.util.Optional;

/** Interfaz CRUD (DIP: service depende de esto, nunca de ActivoRepositorySQLite). */
public interface ActivoRepository {
    void guardar(Activo activo);
    Optional<Activo> buscarPorId(int id);
    List<Activo> listarTodos();
    void actualizar(Activo activo);
    void eliminar(int id);
}