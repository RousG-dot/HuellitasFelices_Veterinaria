package com.pe.vet.veterinaria.servlet;

import com.pe.vet.veterinaria.dao.ClienteDAO;
import com.pe.vet.veterinaria.dao.MascotaDAO;
import com.pe.vet.veterinaria.model.Cliente;
import com.pe.vet.veterinaria.dto.CitaDTO;
import com.pe.vet.veterinaria.model.Mascota;
import com.pe.vet.veterinaria.service.CitaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/CitaServlet")
public class CitaServlet extends HttpServlet {

    private final CitaService citaService = new CitaService();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final MascotaDAO mascotaDAO = new MascotaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String vista = request.getParameter("vista");

        if ("registro".equals(vista)) {
            cargarFormulario(request);
            request.getRequestDispatcher("registroCita.jsp").forward(request, response);
            return;
        }

        List<CitaDTO> listaCitas = citaService.listarCitas();
        request.setAttribute("listaCitas", listaCitas);
        request.getRequestDispatcher("listarCitas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if ("registrar".equals(accion)) {
            CitaDTO cita = construirCitaDesdeRequest(request);
            boolean registrada = citaService.registrarCita(cita);

            if (registrada) {
                response.sendRedirect("CitaServlet?msg=registrada");
            } else {
                response.sendRedirect("CitaServlet?vista=registro&error=datos_invalidos"
                        + "&clienteId=" + encode(request.getParameter("clienteId"))
                        + "&mascotaId=" + encode(request.getParameter("mascotaId"))
                        + "&fecha=" + encode(request.getParameter("fecha"))
                        + "&hora=" + encode(request.getParameter("hora"))
                        + "&motivo=" + encode(request.getParameter("motivo")));
            }
            return;
        }

        if ("actualizar".equals(accion)) {
            CitaDTO cita = construirCitaDesdeRequest(request);
            cita.setId(parseEntero(request.getParameter("id")));
            boolean actualizada = citaService.actualizarCita(cita);

            if (actualizada) {
                response.sendRedirect("CitaServlet?msg=actualizada");
            } else {
                response.sendRedirect("CitaServlet?vista=registro&error=datos_invalidos"
                        + "&id=" + cita.getId()
                        + "&clienteId=" + encode(request.getParameter("clienteId"))
                        + "&mascotaId=" + encode(request.getParameter("mascotaId"))
                        + "&fecha=" + encode(request.getParameter("fecha"))
                        + "&hora=" + encode(request.getParameter("hora"))
                        + "&motivo=" + encode(request.getParameter("motivo")));
            }
            return;
        }

        if ("eliminar".equals(accion)) {
            int id = parseEntero(request.getParameter("id"));
            citaService.eliminarCita(id);
            response.sendRedirect("CitaServlet?msg=eliminada");
            return;
        }

        response.sendRedirect("CitaServlet?error=accion_invalida");
    }

    private CitaDTO construirCitaDesdeRequest(HttpServletRequest request) {
        CitaDTO cita = new CitaDTO();
        cita.setClienteId(parseNullableEntero(request.getParameter("clienteId")));
        cita.setMascotaId(parseNullableEntero(request.getParameter("mascotaId")));
        cita.setFecha(request.getParameter("fecha"));
        cita.setHora(request.getParameter("hora"));
        cita.setMotivo(request.getParameter("motivo"));
        return cita;
    }

    private void cargarFormulario(HttpServletRequest request) {
        List<Cliente> listaClientes = clienteDAO.listar();
        List<Mascota> listaMascotas = mascotaDAO.listar();
        request.setAttribute("listaClientes", listaClientes);
        request.setAttribute("listaMascotas", listaMascotas);
    }

    private int parseEntero(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Integer parseNullableEntero(String valor) {
        int numero = parseEntero(valor);
        return numero > 0 ? numero : null;
    }

    private String encode(String valor) {
        if (valor == null) {
            return "";
        }
        return URLEncoder.encode(valor, StandardCharsets.UTF_8);
    }
}
