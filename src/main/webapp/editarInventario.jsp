<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Editar Mascota - PetSociety</title>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editarInventario.css">

    </head>
    <body>
        <c:if test="${producto == null}">
            <c:redirect url="ProductoServlet" />
        </c:if>

        <aside class="sidebar">
            <div class="sidebar-header">PetSociety Admin</div>
            <nav class="nav-links">
                <a href="${pageContext.request.contextPath}/jsf/dashboard.xhtml" class="nav-link">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 3h7v7H3z"></path><path d="M14 3h7v4h-7z"></path><path d="M14 12h7v9h-7z"></path><path d="M3 14h7v7H3z"></path></svg>
                    Dashboard
                </a>
                <a href="ClienteServlet" class="nav-link">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle></svg>
                    Clientes
                </a>
                <a href="mascotas.jsp" class="nav-link">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                    Mascotas
                </a>
                <a href="registroCita.jsp" class="nav-link">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>
                    Citas
                </a>
                <a href="inventario.jsp" class="nav-link active">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="16.5" y1="9.4" x2="7.5" y2="4.21"></line><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path><polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline><line x1="12" y1="22.08" x2="12" y2="12"></line></svg>
                    Inventario
                </a>
                <a href="${pageContext.request.contextPath}/LogoutServlet" class="nav-link danger">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" y1="12" x2="9" y2="12"></line></svg>
                    Salir
                </a>
            </nav>
        </aside>

        <main class="main-content">
            <div class="form-card">
                <h1 class="header-title">Editar Producto</h1>
                <p class="header-subtitle">Modifica los datos del producto en el sistema</p>
                <c:if test="${not empty error}">
                    <p class="header-subtitle" style="color: #d93025;">${error}</p>
                </c:if>

                <form action="${pageContext.request.contextPath}/ProductoServlet" method="POST">
                    <input type="hidden" name="accion" value="actualizar">
                    <input type="hidden" name="id" value="${producto.id}">

                    <div class="form-group">
                        <label>Nombre del producto</label>
                        <input type="text" name="txtnombre" value="${producto.nombre}" required>
                    </div>

                    <div class="form-group">
                        <label>Precio</label>
                        <input type="number" step="0.01" name="txtprecio" value="${not empty txtprecio ? txtprecio : producto.precio}" required>
                    </div>

                    <div class="form-group">
                        <label>Stock</label>
                        <input type="number" name="txtstock" value="${not empty txtstock ? txtstock : producto.stock}" required>
                    </div>

                    <div class="form-group">
                        <label>Categoria</label>
                        <input type="text" name="txtcategoria" value="${producto.categoria}" required>
                    </div>

                    <div class="form-group">
                        <label>Estado</label>
                        <input type="checkbox" name="txtestado" ${producto.estado ? 'checked' : ''}>
                        Disponible
                    </div>

                    <button type="submit" class="btn-primary">Guardar Cambios</button>
                </form>

                <div class="action-footer">
                    <a href="${pageContext.request.contextPath}/jsf/dashboard.xhtml" class="btn-return">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <line x1="19" y1="12" x2="5" y2="12"></line>
                            <polyline points="12 19 5 12 12 5"></polyline>
                        </svg>
                        Volver al Dashboard
                    </a>
                </div>
            </div>
        </main>
    </body>
</html>
