package com.pe.vet.veterinaria.dao;
import com.pe.vet.veterinaria.model.Usuario;
import com.pe.vet.veterinaria.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {
    private static final Logger LOGGER = Logger.getLogger(UsuarioDAO.class.getName());

    public Usuario validar(String correo, String password) {
        Usuario u = null;
        String sql = "SELECT id, correo, password FROM usuarios WHERE correo=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && BCrypt.checkpw(password, rs.getString("password"))) {
                u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setCorreo(rs.getString("correo"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al validar credenciales del usuario.", e);
        }
        return u; // Si no lo encuentra, devuelve null
    }
}
    
       
        
        
    

