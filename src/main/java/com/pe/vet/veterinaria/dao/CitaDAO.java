package com.pe.vet.veterinaria.dao;

import com.pe.vet.veterinaria.model.Cita;
import com.pe.vet.veterinaria.util.Conexion; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CitaDAO {
    private static final Logger LOGGER = Logger.getLogger(CitaDAO.class.getName());

    public List<Cita> listar() {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT c.id, c.cliente, c.mascota, c.fecha, c.hora, c.motivo, c.cliente_id, c.mascota_id, "
                + "COALESCE(CONCAT(cl.nombre, ' ', cl.apellido), c.cliente) AS cliente_visible, "
                + "COALESCE(m.nombre, c.mascota) AS mascota_visible "
                + "FROM citas c "
                + "LEFT JOIN clientes cl ON cl.id = c.cliente_id "
                + "LEFT JOIN mascotas m ON m.id = c.mascota_id";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cita c = new Cita();
                c.setId(rs.getInt("id"));
                int clienteId = rs.getInt("cliente_id");
                c.setClienteId(rs.wasNull() ? null : clienteId);
                int mascotaId = rs.getInt("mascota_id");
                c.setMascotaId(rs.wasNull() ? null : mascotaId);
                c.setCliente(rs.getString("cliente_visible"));
                c.setMascota(rs.getString("mascota_visible"));
                c.setFecha(rs.getString("fecha"));
                c.setHora(rs.getString("hora"));
                c.setMotivo(rs.getString("motivo"));
                lista.add(c);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al listar citas.", e);
        }
        return lista;
    }

    public boolean registrar(Cita c) {
        String sql = "INSERT INTO citas (cliente, mascota, fecha, hora, motivo, cliente_id, mascota_id) VALUES (?,?,?,?,?,?,?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getCliente());
            ps.setString(2, c.getMascota());
            ps.setString(3, c.getFecha());
            ps.setString(4, c.getHora());
            ps.setString(5, c.getMotivo());
            if (c.getClienteId() != null) {
                ps.setInt(6, c.getClienteId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            if (c.getMascotaId() != null) {
                ps.setInt(7, c.getMascotaId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al registrar cita.", e);
            return false;
        }
    }

    public boolean actualizar(Cita c) {
        String sql = "UPDATE citas SET cliente=?, mascota=?, fecha=?, hora=?, motivo=?, cliente_id=?, mascota_id=? WHERE id=?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getCliente());
            ps.setString(2, c.getMascota());
            ps.setString(3, c.getFecha());
            ps.setString(4, c.getHora());
            ps.setString(5, c.getMotivo());
            if (c.getClienteId() != null) {
                ps.setInt(6, c.getClienteId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            if (c.getMascotaId() != null) {
                ps.setInt(7, c.getMascotaId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setInt(8, c.getId());

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar cita.", e);
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM citas WHERE id=?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar cita.", e);
            return false;
        }
    }
}
