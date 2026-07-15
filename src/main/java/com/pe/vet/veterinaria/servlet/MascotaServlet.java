package com.pe.vet.veterinaria.servlet;

import com.pe.vet.veterinaria.dao.ClienteDAO;
import com.pe.vet.veterinaria.model.Cliente;
import com.pe.vet.veterinaria.model.Mascota;
import com.pe.vet.veterinaria.service.MascotaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "MascotaServlet", urlPatterns = {"/MascotaServlet"})
public class MascotaServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(MascotaServlet.class.getName());
    private static final String FORM_REGISTRO = "registroMascota.jsp";
    private static final String FORM_EDICION = "editarMascota.jsp";
    private static final String LISTADO = "mascotas.jsp";
    private final MascotaService mascotaService = new MascotaService();

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

        if ("registrar".equals(accion)) {
            procesarRegistro(request, response);
            return;
        }

        if ("actualizar".equals(accion)) {
            procesarActualizacion(request, response);
            return;
        }

        if ("eliminar".equals(accion)) {
            procesarEliminacion(request, response);
            return;
        }

        listarMascotasConMensaje(request, response, "Accion no valida.");
    }

    private void listarMascotas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("listaMascotas", mascotaService.listarMascotas());
        request.getRequestDispatcher(LISTADO).forward(request, response);
    }

    private void listarMascotasConMensaje(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("mensaje", mensaje);
        listarMascotas(request, response);
    }

    private void cargarFormularioRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> listaClientes = clienteDAO.listar();
        request.setAttribute("listaClientes", listaClientes);
        request.getRequestDispatcher(FORM_REGISTRO).forward(request, response);
    }

    private void cargarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = parsePositivo(request.getParameter("id"));
        if (id == null) {
            listarMascotasConMensaje(request, response, "La mascota indicada no es valida.");
            return;
        }

        ClienteDAO clienteDAO = new ClienteDAO();
        Mascota mascota = mascotaService.obtenerMascota(id);

        if (mascota == null) {
            listarMascotasConMensaje(request, response, "La mascota indicada no existe.");
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
        request.getRequestDispatcher(FORM_EDICION).forward(request, response);
    }

    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            if (!mascotaService.registrarMascota(
                    request.getParameter("clienteId"),
                    request.getParameter("txtnombre"),
                    request.getParameter("txtespecie"),
                    request.getParameter("txtraza"))) {
                throw new IllegalStateException("No se pudo registrar la mascota.");
            }
            response.sendRedirect("MascotaServlet?msg=registrada");
        } catch (IllegalArgumentException e) {
            reenviarRegistroConError(request, response, e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error tecnico al registrar mascota.", e);
            reenviarRegistroConError(request, response, "Ocurrio un problema al registrar la mascota.");
        }
    }

    private void procesarActualizacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            if (!mascotaService.actualizarMascota(
                    request.getParameter("id"),
                    request.getParameter("clienteId"),
                    request.getParameter("txtnombre"),
                    request.getParameter("txtespecie"),
                    request.getParameter("txtraza"))) {
                throw new IllegalStateException("No se pudo actualizar la mascota.");
            }
            response.sendRedirect("MascotaServlet?msg=actualizada");
        } catch (IllegalArgumentException e) {
            reenviarEdicionConError(request, response, e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error tecnico al actualizar mascota.", e);
            reenviarEdicionConError(request, response, "Ocurrio un problema al actualizar la mascota.");
        }
    }

    private void procesarEliminacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (!mascotaService.eliminarMascota(request.getParameter("id"))) {
                throw new IllegalStateException("No se pudo eliminar la mascota.");
            }

            response.sendRedirect("MascotaServlet?msg=eliminada");
        } catch (IllegalArgumentException e) {
            listarMascotasConMensaje(request, response, e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error tecnico al eliminar mascota.", e);
            listarMascotasConMensaje(request, response, "Ocurrio un problema al eliminar la mascota.");
        }
    }

    private void reenviarRegistroConError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.setAttribute("txtnombre", request.getParameter("txtnombre"));
        request.setAttribute("txtespecie", request.getParameter("txtespecie"));
        request.setAttribute("txtraza", request.getParameter("txtraza"));
        request.setAttribute("clienteId", request.getParameter("clienteId"));
        cargarFormularioRegistro(request, response);
    }

    private void reenviarEdicionConError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        ClienteDAO clienteDAO = new ClienteDAO();
        Mascota mascota = new Mascota();
        Integer id = parsePositivo(request.getParameter("id"));
        if (id != null) {
            mascota.setId(id);
        }
        mascota.setNombre(request.getParameter("txtnombre"));
        mascota.setEspecie(request.getParameter("txtespecie"));
        mascota.setRaza(request.getParameter("txtraza"));
        mascota.setClienteId(parsePositivo(request.getParameter("clienteId")));

        request.setAttribute("error", mensaje);
        request.setAttribute("mascota", mascota);
        request.setAttribute("listaClientes", clienteDAO.listar());
        request.getRequestDispatcher(FORM_EDICION).forward(request, response);
    }

    private Integer parsePositivo(String valor) {
        String valorNormalizado = normalizar(valor);
        if (valorNormalizado == null || valorNormalizado.isEmpty()) {
            return null;
        }

        try {
            int numero = Integer.parseInt(valorNormalizado);
            return numero > 0 ? numero : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
