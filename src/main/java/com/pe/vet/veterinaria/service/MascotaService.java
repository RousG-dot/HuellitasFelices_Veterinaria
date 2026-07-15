package com.pe.vet.veterinaria.service;

import com.pe.vet.veterinaria.dao.ClienteDAO;
import com.pe.vet.veterinaria.dao.MascotaDAO;
import com.pe.vet.veterinaria.model.Cliente;
import com.pe.vet.veterinaria.model.Mascota;
import java.util.List;

public class MascotaService {
    private final MascotaDAO mascotaDAO = new MascotaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    public List<Mascota> listarMascotas() {
        return mascotaDAO.listar();
    }

    public Mascota obtenerMascota(String idTexto) {
        Integer id = parsePositivo(idTexto, "La mascota indicada no es valida.");
        return obtenerMascota(id);
    }

    public Mascota obtenerMascota(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        return mascotaDAO.obtenerPorId(id);
    }

    public boolean registrarMascota(String clienteIdTexto, String nombre, String especie, String raza) {
        Mascota mascota = construirMascota(clienteIdTexto, nombre, especie, raza, null, false);
        return mascotaDAO.registrar(mascota);
    }

    public boolean actualizarMascota(String idTexto, String clienteIdTexto, String nombre, String especie, String raza) {
        Integer id = parsePositivo(idTexto, "La mascota indicada no es valida.");
        Mascota actual = obtenerMascota(id);
        if (actual == null) {
            throw new IllegalArgumentException("La mascota indicada no existe.");
        }

        Mascota mascota = construirMascota(clienteIdTexto, nombre, especie, raza, actual.getId(), true);
        return mascotaDAO.actualizar(mascota);
    }

    public boolean eliminarMascota(String idTexto) {
        Integer id = parsePositivo(idTexto, "La mascota indicada no es valida.");
        Mascota actual = obtenerMascota(id);
        if (actual == null) {
            throw new IllegalArgumentException("La mascota indicada no existe.");
        }
        return mascotaDAO.eliminar(actual.getId());
    }

    private Mascota construirMascota(String clienteIdTexto, String nombre, String especie, String raza, Integer id, boolean requiereId) {
        Integer clienteId = parsePositivo(clienteIdTexto, "Seleccione un cliente valido.");
        Cliente cliente = clienteDAO.obtenerPorId(clienteId);
        if (cliente == null) {
            throw new IllegalArgumentException("Seleccione un cliente valido.");
        }

        String nombreNormalizado = normalizar(nombre);
        if (nombreNormalizado == null || nombreNormalizado.isEmpty() || nombreNormalizado.length() > 50) {
            throw new IllegalArgumentException("Ingrese un nombre valido para la mascota.");
        }

        String especieNormalizada = normalizar(especie);
        if (especieNormalizada == null || especieNormalizada.isEmpty() || especieNormalizada.length() > 30) {
            throw new IllegalArgumentException("Ingrese una especie valida.");
        }

        String razaNormalizada = normalizar(raza);
        if (razaNormalizada != null && razaNormalizada.length() > 50) {
            throw new IllegalArgumentException("Ingrese una raza valida.");
        }
        if (razaNormalizada != null && razaNormalizada.isEmpty()) {
            razaNormalizada = null;
        }

        Mascota mascota = new Mascota();
        if (requiereId) {
            mascota.setId(id);
        }
        mascota.setNombre(nombreNormalizado);
        mascota.setEspecie(especieNormalizada);
        mascota.setRaza(razaNormalizada);
        mascota.setClienteId(clienteId);
        mascota.setDueno((cliente.getNombre() + " " + cliente.getApellido()).trim());
        return mascota;
    }

    private Integer parsePositivo(String valor, String mensajeError) {
        String valorNormalizado = normalizar(valor);
        if (valorNormalizado == null || valorNormalizado.isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }

        try {
            int numero = Integer.parseInt(valorNormalizado);
            if (numero <= 0) {
                throw new IllegalArgumentException(mensajeError);
            }
            return numero;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
