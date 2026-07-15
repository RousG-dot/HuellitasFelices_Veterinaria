package com.pe.vet.veterinaria.servlet;

import com.pe.vet.veterinaria.dao.UsuarioDAO;
import com.pe.vet.veterinaria.model.Usuario;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            response.sendRedirect(request.getContextPath() + "/jsf/dashboard.xhtml");
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("txtemail");
        String password = request.getParameter("txtpass");
        String correo = email == null ? "" : email.trim();

        if (correo.isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Ingrese correo y contraseña.");
            request.setAttribute("correoIngresado", correo);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        Usuario user = dao.validar(correo, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", user);
            response.sendRedirect(request.getContextPath() + "/jsf/dashboard.xhtml");
        } else {
            request.setAttribute("error", "Correo o contraseña incorrectos.");
            request.setAttribute("correoIngresado", correo);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
