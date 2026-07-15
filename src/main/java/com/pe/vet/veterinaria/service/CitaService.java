package com.pe.vet.veterinaria.service;

import com.pe.vet.veterinaria.dao.ClienteDAO;
import com.pe.vet.veterinaria.dao.CitaDAO;
import com.pe.vet.veterinaria.dao.MascotaDAO;
import com.pe.vet.veterinaria.dto.CitaDTO;
import com.pe.vet.veterinaria.model.Cita;
import com.pe.vet.veterinaria.model.Cliente;
import com.pe.vet.veterinaria.model.Mascota;
import java.util.ArrayList;
import java.util.List;

public class CitaService {
    private final CitaDAO dao = new CitaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final MascotaDAO mascotaDAO = new MascotaDAO();

    public List<CitaDTO> listarCitas() {
        List<Cita> citas = dao.listar();
        List<CitaDTO> citasDto = new ArrayList<>();

        for (Cita cita : citas) {
            CitaDTO dto = new CitaDTO();
            dto.setId(cita.getId());
            dto.setClienteId(cita.getClienteId());
            dto.setMascotaId(cita.getMascotaId());
            dto.setCliente(cita.getCliente());
            dto.setMascota(cita.getMascota());
            dto.setFecha(cita.getFecha());
            dto.setHora(cita.getHora());
            dto.setMotivo(cita.getMotivo());
            citasDto.add(dto);
        }

        return citasDto;
    }

    public boolean registrarCita(CitaDTO dto) {
        Cita cita = construirCita(dto);
        if (cita == null) {
            return false;
        }

        return dao.registrar(cita);
    }

    public boolean actualizarCita(CitaDTO dto) {
        if (dto == null || dto.getId() <= 0) {
            return false;
        }

        Cita cita = construirCita(dto);
        if (cita == null) {
            return false;
        }

        cita.setId(dto.getId());

        return dao.actualizar(cita);
    }

    public boolean eliminarCita(int id) {
        if (id <= 0) {
            return false;
        }

        return dao.eliminar(id);
    }

    private Cita construirCita(CitaDTO dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getClienteId() == null || dto.getClienteId() <= 0) {
            return null;
        }
        if (dto.getMascotaId() == null || dto.getMascotaId() <= 0) {
            return null;
        }
        if (dto.getFecha() == null || dto.getFecha().trim().isEmpty()) {
            return null;
        }
        if (dto.getHora() == null || dto.getHora().trim().isEmpty()) {
            return null;
        }

        Cliente cliente = clienteDAO.obtenerPorId(dto.getClienteId());
        if (cliente == null) {
            return null;
        }

        Mascota mascota = mascotaDAO.obtenerPorId(dto.getMascotaId());
        if (mascota == null || mascota.getClienteId() == null || !dto.getClienteId().equals(mascota.getClienteId())) {
            return null;
        }

        Cita cita = new Cita();
        cita.setClienteId(cliente.getId());
        cita.setMascotaId(mascota.getId());
        cita.setCliente((cliente.getNombre() + " " + cliente.getApellido()).trim());
        cita.setMascota(mascota.getNombre());
        cita.setFecha(dto.getFecha().trim());
        cita.setHora(dto.getHora().trim());
        cita.setMotivo(normalizar(dto.getMotivo()));
        return cita;
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return null;
        }
        return valor.trim();
    }
}
