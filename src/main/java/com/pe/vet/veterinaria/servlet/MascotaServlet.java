package com.pe.vet.veterinaria.servlet;

import com.pe.vet.veterinaria.dao.ClienteDAO;
import com.pe.vet.veterinaria.dao.MascotaDAO;
import com.pe.vet.veterinaria.model.Cliente;
import com.pe.vet.veterinaria.model.Mascota;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MascotaServlet", urlPatterns = {"/MascotaServlet"})
public class MascotaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String vista = request.getParameter("vista");

        if ("registro".equals(vista)) {
            cargarFormularioRegistro(request, response);
            return;
        }

        if ("editar".equals(vista)) {
            cargarFormularioEdicion(request, response);
            return;
        }

        listarMascotas(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        MascotaDAO mascotaDAO = new MascotaDAO();

        if ("registrar".equals(accion)) {
            procesarRegistro(request, response, mascotaDAO);
            return;
        }

        if ("actualizar".equals(accion)) {
            procesarActualizacion(request, response, mascotaDAO);
            return;
        }

        if ("eliminar".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            mascotaDAO.eliminar(id);
        }

        response.sendRedirect("MascotaServlet");
    }

    private void listarMascotas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MascotaDAO mascotaDAO = new MascotaDAO();
        request.setAttribute("listaMascotas", mascotaDAO.listar());
        request.getRequestDispatcher("mascotas.jsp").forward(request, response);
    }

    private void cargarFormularioRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> listaClientes = clienteDAO.listar();
        request.setAttribute("listaClientes", listaClientes);
        request.getRequestDispatcher("registroMascota.jsp").forward(request, response);
    }

    private void cargarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("MascotaServlet");
            return;
        }

        MascotaDAO mascotaDAO = new MascotaDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        Mascota mascota = mascotaDAO.obtenerPorId(Integer.parseInt(idParam));

        if (mascota == null) {
            response.sendRedirect("MascotaServlet");
            return;
        }

        List<Cliente> listaClientes = clienteDAO.listar();

        if (mascota.getClienteId() == null && mascota.getDueno() != null) {
            String duenoActual = mascota.getDueno().trim();
            for (Cliente cliente : listaClientes) {
                String nombreCompleto = (cliente.getNombre() + " " + cliente.getApellido()).trim();
                if (nombreCompleto.equalsIgnoreCase(duenoActual)) {
                    mascota.setClienteId(cliente.getId());
                    break;
                }
            }
        }

        request.setAttribute("mascota", mascota);
        request.setAttribute("listaClientes", listaClientes);
        request.getRequestDispatcher("editarMascota.jsp").forward(request, response);
    }

    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response, MascotaDAO mascotaDAO)
            throws IOException {
        String nombre = request.getParameter("txtnombre");
        String especie = request.getParameter("txtespecie");
        String raza = request.getParameter("txtraza");
        String clienteIdParam = request.getParameter("clienteId");

        Integer clienteId = parseClienteId(clienteIdParam);
        if (clienteId == null) {
            response.sendRedirect("MascotaServlet?vista=registro&error=dueno_requerido");
            return;
        }

        String nombreCompletoDueno = mascotaDAO.obtenerNombreCompletoCliente(clienteId);
        if (nombreCompletoDueno == null || nombreCompletoDueno.trim().isEmpty()) {
            response.sendRedirect("MascotaServlet?vista=registro&error=dueno_invalido");
            return;
        }

        Mascota mascota = new Mascota();
        mascota.setNombre(nombre);
        mascota.setEspecie(especie);
        mascota.setRaza(raza);
        mascota.setClienteId(clienteId);
        mascota.setDueno(nombreCompletoDueno);
        mascotaDAO.registrar(mascota);

        response.sendRedirect("MascotaServlet");
    }

    private void procesarActualizacion(HttpServletRequest request, HttpServletResponse response, MascotaDAO mascotaDAO)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("txtnombre");
        String especie = request.getParameter("txtespecie");
        String raza = request.getParameter("txtraza");
        String clienteIdParam = request.getParameter("clienteId");

        Integer clienteId = parseClienteId(clienteIdParam);
        if (clienteId == null) {
            response.sendRedirect("MascotaServlet?vista=editar&id=" + id + "&error=dueno_requerido");
            return;
        }

        String nombreCompletoDueno = mascotaDAO.obtenerNombreCompletoCliente(clienteId);
        if (nombreCompletoDueno == null || nombreCompletoDueno.trim().isEmpty()) {
            response.sendRedirect("MascotaServlet?vista=editar&id=" + id + "&error=dueno_invalido");
            return;
        }

        Mascota mascota = new Mascota();
        mascota.setId(id);
        mascota.setNombre(nombre);
        mascota.setEspecie(especie);
        mascota.setRaza(raza);
        mascota.setClienteId(clienteId);
        mascota.setDueno(nombreCompletoDueno);
        mascotaDAO.actualizar(mascota);

        response.sendRedirect("MascotaServlet");
    }

    private Integer parseClienteId(String clienteIdParam) {
        if (clienteIdParam == null || clienteIdParam.trim().isEmpty()) {
            return null;
        }

        try {
            int clienteId = Integer.parseInt(clienteIdParam);
            return clienteId > 0 ? clienteId : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
