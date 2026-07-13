package com.pe.vet.veterinaria.dao;

import com.pe.vet.veterinaria.model.Mascota;
import com.pe.vet.veterinaria.util.Conexion;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MascotaDAO {

    public boolean registrar(Mascota mascota) {
        boolean usaClienteId = existeColumnaClienteId();
        String sql = usaClienteId
                ? "INSERT INTO mascotas (nombre, especie, raza, dueno, cliente_id) VALUES (?,?,?,?,?)"
                : "INSERT INTO mascotas (nombre, especie, raza, dueno) VALUES (?,?,?,?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, mascota.getNombre());
            ps.setString(2, mascota.getEspecie());
            ps.setString(3, mascota.getRaza());
            ps.setString(4, mascota.getDueno());

            if (usaClienteId) {
                ps.setInt(5, mascota.getClienteId());
            }

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al registrar mascota: " + e.getMessage());
            return false;
        }
    }

    public List<Mascota> listar() {
        List<Mascota> lista = new ArrayList<>();
        boolean usaClienteId = existeColumnaClienteId();
        String sql = usaClienteId
                ? "SELECT id, nombre, especie, raza, dueno, cliente_id FROM mascotas"
                : "SELECT id, nombre, especie, raza, dueno FROM mascotas";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearMascota(rs, usaClienteId));
            }
        } catch (Exception e) {
            System.out.println("Error al listar mascotas: " + e.getMessage());
        }
        return lista;
    }

    public Mascota obtenerPorId(int id) {
        boolean usaClienteId = existeColumnaClienteId();
        String sql = usaClienteId
                ? "SELECT id, nombre, especie, raza, dueno, cliente_id FROM mascotas WHERE id=?"
                : "SELECT id, nombre, especie, raza, dueno FROM mascotas WHERE id=?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMascota(rs, usaClienteId);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener mascota: " + e.getMessage());
        }

        return null;
    }

    public boolean actualizar(Mascota mascota) {
        boolean usaClienteId = existeColumnaClienteId();
        String sql = usaClienteId
                ? "UPDATE mascotas SET nombre=?, especie=?, raza=?, dueno=?, cliente_id=? WHERE id=?"
                : "UPDATE mascotas SET nombre=?, especie=?, raza=?, dueno=? WHERE id=?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, mascota.getNombre());
            ps.setString(2, mascota.getEspecie());
            ps.setString(3, mascota.getRaza());
            ps.setString(4, mascota.getDueno());

            if (usaClienteId) {
                ps.setInt(5, mascota.getClienteId());
                ps.setInt(6, mascota.getId());
            } else {
                ps.setInt(5, mascota.getId());
            }

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al actualizar mascota: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM mascotas WHERE id=?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al eliminar mascota: " + e.getMessage());
            return false;
        }
    }

    public boolean existeColumnaClienteId() {
        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return false;
            }

            DatabaseMetaData metaData = con.getMetaData();
            try (ResultSet rs = metaData.getColumns(con.getCatalog(), null, "mascotas", "cliente_id")) {
                return rs.next();
            }
        } catch (Exception e) {
            System.out.println("No se pudo validar la columna cliente_id: " + e.getMessage());
            return false;
        }
    }

    public String obtenerNombreCompletoCliente(int clienteId) {
        String sql = "SELECT nombre, apellido FROM clientes WHERE id=?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, clienteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    return (nombre + " " + apellido).trim();
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener dueño de mascota: " + e.getMessage());
        }

        return null;
    }

    private Mascota mapearMascota(ResultSet rs, boolean usaClienteId) throws Exception {
        Mascota mascota = new Mascota();
        mascota.setId(rs.getInt("id"));
        mascota.setNombre(rs.getString("nombre"));
        mascota.setEspecie(rs.getString("especie"));
        mascota.setRaza(rs.getString("raza"));
        mascota.setDueno(rs.getString("dueno"));

        if (usaClienteId) {
            int clienteId = rs.getInt("cliente_id");
            mascota.setClienteId(rs.wasNull() ? null : clienteId);
        }

        return mascota;
    }
}
