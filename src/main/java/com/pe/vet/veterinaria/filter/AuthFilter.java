package com.pe.vet.veterinaria.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter extends HttpFilter implements Filter {

    private static final String LOGIN_ATTRIBUTE = "usuarioLogueado";

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String contextPath = request.getContextPath();
        String requestUri = request.getRequestURI();
        String path = requestUri.substring(contextPath.length());

        if (isPublicPath(path) || isAuthenticated(request)) {
            chain.doFilter(request, response);
            return;
        }

        response.sendRedirect(contextPath + "/login.jsp");
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(LOGIN_ATTRIBUTE) != null;
    }

    private boolean isPublicPath(String path) {
        if (path == null || path.isEmpty() || "/".equals(path)) {
            return true;
        }

        if ("/index.jsp".equals(path) || "/login.jsp".equals(path)
                || "/LoginServlet".equals(path) || "/LogoutServlet".equals(path)) {
            return true;
        }

        if (path.startsWith("/css/") || path.startsWith("/img/") || path.startsWith("/js/")
                || path.startsWith("/jakarta.faces.resource/")) {
            return true;
        }

        return path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".png")
                || path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".gif")
                || path.endsWith(".svg") || path.endsWith(".ico") || path.endsWith(".woff")
                || path.endsWith(".woff2") || path.endsWith(".ttf");
    }
}
