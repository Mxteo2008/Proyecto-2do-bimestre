package repository;

import model.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActivoRepositorySQLite implements ActivoRepository, Reportable {

    private final Connection conexion;

    public ActivoRepositorySQLite(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void guardar(Activo activo) {
        String sql = "INSERT INTO activos (tipo, nombre, fecha_adquisicion, costo_base, estado,\n" + "                      vida_util_anios, marca, tipo_conexion, fecha_expiracion, numero_licencias)\n" + "VALUES (?,?,?,?,?,?,?,?,?,?)\n";
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            enlazarParametros(ps, activo);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    activo.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RepositorioException("Error al guardar el activo", e);
        }
    }

    @Override
    public Optional<Activo> buscarPorId(int id) {
        String sql = "SELECT * FROM activos WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearActivo(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositorioException("Error al buscar el activo con id " + id, e);
        }
    }

    @Override
    public List<Activo> listarTodos() {
        String sql = "SELECT * FROM activos ORDER BY id";
        List<Activo> resultado = new ArrayList<>();
        try (Statement stmt = conexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resultado.add(mapearActivo(rs));
            }
        } catch (SQLException e) {
            throw new RepositorioException("Error al listar los activos", e);
        }
        return resultado;
    }

  
    @Override
    public void actualizar(Activo activo) {
        String sql = "UPDATE activos SET tipo=?, nombre=?, fecha_adquisicion=?, costo_base=?, estado=?,\n" + "       vida_util_anios=?, marca=?, tipo_conexion=?, fecha_expiracion=?, numero_licencias=?\n" + "WHERE id=?\n";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            enlazarParametros(ps, activo);
            ps.setInt(11, activo.getId());
            if (ps.executeUpdate() == 0) {
                throw new RepositorioException("No existe activo con id " + activo.getId());
            }
        } catch (SQLException e) {
            throw new RepositorioException("Error al actualizar el activo", e);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM activos WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new RepositorioException("No existe activo con id " + id);
            }
        } catch (SQLException e) {
            throw new RepositorioException("Error al eliminar el activo", e);
        }
    }

    @Override
    public List<Activo> filtrarPorTipo(TipoActivo tipo) {
        String sql = "SELECT * FROM activos WHERE tipo = ? ORDER BY id";
        List<Activo> resultado = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, tipo.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapearActivo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RepositorioException("Error al filtrar por tipo " + tipo, e);
        }
        return resultado;
    }

    public double costoTotalMantenimiento() {
        double total = 0;
        for (Activo activo : listarTodos()) {
            total += activo.calcularCostoMantenimiento();
        }
        return total;
    }

    private void enlazarParametros(PreparedStatement ps, Activo activo) throws SQLException {
        ps.setString(1, activo.getTipo().name());
        ps.setString(2, activo.getNombre());
        ps.setString(3, activo.getFechaAdquisicion() != null ? activo.getFechaAdquisicion().toString() : null);
        ps.setDouble(4, activo.getCostoBase());
        ps.setString(5, activo.getEstado());

        if (activo instanceof Hardware hw) {
            ps.setInt(6, hw.getVidaUtilAnios());
            ps.setString(7, hw.getMarca());
            ps.setNull(8, Types.VARCHAR);
            ps.setNull(9, Types.VARCHAR);
            ps.setNull(10, Types.INTEGER);
        } else if (activo instanceof Periferico per) {
            ps.setNull(6, Types.INTEGER);
            ps.setNull(7, Types.VARCHAR);
            ps.setString(8, per.getTipoConexion());
            ps.setNull(9, Types.VARCHAR);
            ps.setNull(10, Types.INTEGER);
        } else if (activo instanceof Licencia lic) {
            ps.setNull(6, Types.INTEGER);
            ps.setNull(7, Types.VARCHAR);
            ps.setNull(8, Types.VARCHAR);
            ps.setString(9, lic.getFechaExpiracion() != null ? lic.getFechaExpiracion().toString() : null);
            ps.setInt(10, lic.getNumeroLicencias());
        } else {
            throw new RepositorioException("Tipo de activo no soportado: " + activo.getClass());
        }
    }

    private Activo mapearActivo(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String tipo = rs.getString("tipo");
        String nombre = rs.getString("nombre");
        String fechaAdqStr = rs.getString("fecha_adquisicion");
        LocalDate fechaAdquisicion = fechaAdqStr != null ? LocalDate.parse(fechaAdqStr) : null;
        double costoBase = rs.getDouble("costo_base");
        String estado = rs.getString("estado");

        return switch (TipoActivo.valueOf(tipo)) {
            case HARDWARE ->
                new Hardware(id, nombre, fechaAdquisicion, costoBase, estado,
                rs.getInt("vida_util_anios"), rs.getString("marca"));
            case PERIFERICO ->
                new Periferico(id, nombre, fechaAdquisicion, costoBase, estado,
                rs.getString("tipo_conexion"));
            case LICENCIA -> {
                String fechaExpStr = rs.getString("fecha_expiracion");
                LocalDate fechaExp = fechaExpStr != null ? LocalDate.parse(fechaExpStr) : null;
                yield new Licencia(id, nombre, fechaAdquisicion, costoBase, estado,
                fechaExp, rs.getInt("numero_licencias"));
            }
        };
    }
}
