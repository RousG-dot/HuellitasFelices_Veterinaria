package com.pe.vet.veterinaria.servlet;

import com.pe.vet.veterinaria.model.Producto;
import com.pe.vet.veterinaria.service.ProductoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ProductoServelt", urlPatterns = {"/ProductoServlet"})
public class ProductoServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ProductoServlet.class.getName());
    private final ProductoService productoService = new ProductoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String vista = request.getParameter("vista");

        if ("registro".equals(vista)) {
            request.getRequestDispatcher("registroInventario.jsp").forward(request, response);
            return;
        }

        if ("editar".equals(vista)) {
            cargarFormularioEdicion(request, response);
            return;
        }

        cargarListado(request, response);
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

        cargarListadoConMensaje(request, response, "Accion no valida.");
    }

    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            boolean registrado = productoService.registrarProducto(
                    request.getParameter("txtnombre"),
                    request.getParameter("txtprecio"),
                    request.getParameter("txtstock"),
                    request.getParameter("txtcategoria"),
                    request.getParameter("txtestado") != null
            );

            if (registrado) {
                response.sendRedirect("ProductoServlet?msg=registrado");
                return;
            }

            cargarFormularioRegistroConError(request, response, "No se pudo registrar el producto.");
        } catch (IllegalArgumentException e) {
            cargarFormularioRegistroConError(request, response, e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error tecnico al registrar producto.", e);
            cargarFormularioRegistroConError(request, response, "Ocurrio un problema al registrar el producto.");
        }
    }

    private void procesarActualizacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            boolean actualizado = productoService.actualizarProducto(
                    request.getParameter("id"),
                    request.getParameter("txtnombre"),
                    request.getParameter("txtprecio"),
                    request.getParameter("txtstock"),
                    request.getParameter("txtcategoria"),
                    request.getParameter("txtestado") != null
            );

            if (actualizado) {
                response.sendRedirect("ProductoServlet?msg=actualizado");
                return;
            }

            cargarFormularioEdicionConError(request, response, "No se pudo actualizar el producto.");
        } catch (IllegalArgumentException e) {
            cargarFormularioEdicionConError(request, response, e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error tecnico al actualizar producto.", e);
            cargarFormularioEdicionConError(request, response, "Ocurrio un problema al actualizar el producto.");
        }
    }

    private void procesarEliminacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            boolean eliminado = productoService.eliminarProducto(request.getParameter("id"));
            if (eliminado) {
                response.sendRedirect("ProductoServlet?msg=eliminado");
                return;
            }
            cargarListadoConMensaje(request, response, "No se pudo eliminar el producto.");
        } catch (IllegalArgumentException e) {
            cargarListadoConMensaje(request, response, e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error tecnico al eliminar producto.", e);
            cargarListadoConMensaje(request, response, "Ocurrio un problema al eliminar el producto.");
        }
    }

    private void cargarListado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Producto> lista = productoService.listarProductos();
        request.setAttribute("lista", lista);
        request.getRequestDispatcher("inventario.jsp").forward(request, response);
    }

    private void cargarListadoConMensaje(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("mensaje", mensaje);
        cargarListado(request, response);
    }

    private void cargarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Producto producto = productoService.obtenerProducto(parseId(request.getParameter("id")));
        if (producto == null) {
            cargarListadoConMensaje(request, response, "El producto seleccionado no existe.");
            return;
        }

        request.setAttribute("producto", producto);
        request.getRequestDispatcher("editarInventario.jsp").forward(request, response);
    }

    private void cargarFormularioRegistroConError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.setAttribute("txtnombre", request.getParameter("txtnombre"));
        request.setAttribute("txtprecio", request.getParameter("txtprecio"));
        request.setAttribute("txtstock", request.getParameter("txtstock"));
        request.setAttribute("txtcategoria", request.getParameter("txtcategoria"));
        request.setAttribute("txtestado", request.getParameter("txtestado") != null);
        request.getRequestDispatcher("registroInventario.jsp").forward(request, response);
    }

    private void cargarFormularioEdicionConError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        Producto producto = new Producto();
        producto.setId(parseId(request.getParameter("id")));
        producto.setNombre(request.getParameter("txtnombre"));
        producto.setCategoria(request.getParameter("txtcategoria"));
        producto.setEstado(request.getParameter("txtestado") != null);
        producto.setPrecio(parseDoubleSeguro(request.getParameter("txtprecio")));
        producto.setStock(parseIntSeguro(request.getParameter("txtstock")));

        request.setAttribute("error", mensaje);
        request.setAttribute("producto", producto);
        request.setAttribute("txtprecio", request.getParameter("txtprecio"));
        request.setAttribute("txtstock", request.getParameter("txtstock"));
        request.getRequestDispatcher("editarInventario.jsp").forward(request, response);
    }

    private int parseId(String valor) {
        return parseIntSeguro(valor);
    }

    private int parseIntSeguro(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDoubleSeguro(String valor) {
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
