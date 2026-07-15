<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Agenda de Citas - PetSociety</title>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/registroCita.css">
    </head>
    <body>
        <c:if test="${empty listaClientes or empty listaMascotas}">
            <c:redirect url="CitaServlet">
                <c:param name="vista" value="registro" />
            </c:redirect>
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
                <a href="CitaServlet?vista=registro" class="nav-link active">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>
                    Citas
                </a>
                <a href="inventario.jsp" class="nav-link">
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
                <h1 class="header-title">${empty param.id ? 'Programar Cita Medica' : 'Editar Cita Medica'}</h1>
                <c:if test="${param.error == 'datos_invalidos'}">
                    <p class="header-subtitle">No se pudo guardar la cita. Verifica cliente, mascota, fecha, hora y que la mascota pertenezca al cliente.</p>
                </c:if>

                <form action="${pageContext.request.contextPath}/CitaServlet" method="POST">
                    <input type="hidden" name="accion" value="${empty param.id ? 'registrar' : 'actualizar'}">
                    <c:if test="${not empty param.id}">
                        <input type="hidden" name="id" value="${param.id}">
                    </c:if>

                    <div class="form-group">
                        <label>Cliente</label>
                        <select name="clienteId" required>
                            <option value="">Seleccione un cliente</option>
                            <c:forEach var="cliente" items="${listaClientes}">
                                <option value="${cliente.id}" ${param.clienteId == cliente.id ? 'selected' : ''}>
                                    ${cliente.nombre} ${cliente.apellido} - ${cliente.dni}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Seleccionar Mascota</label>
                        <select name="mascotaId" required>
                            <option value="">Seleccione una mascota</option>
                            <c:forEach var="mascota" items="${listaMascotas}">
                                <option value="${mascota.id}" ${param.mascotaId == mascota.id ? 'selected' : ''}>
                                    ${mascota.nombre} - ${mascota.dueno}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Fecha</label>
                        <input type="date" name="fecha" value="${param.fecha}" required>
                    </div>

                    <div class="form-group">
                        <label>Hora</label>
                        <select name="hora" required>
                            <option value="">Seleccione una hora</option>
                            <option value="09:00" ${param.hora == '09:00' ? 'selected' : ''}>09:00 AM</option>
                            <option value="09:30" ${param.hora == '09:30' ? 'selected' : ''}>09:30 AM</option>
                            <option value="10:00" ${param.hora == '10:00' ? 'selected' : ''}>10:00 AM</option>
                            <option value="10:30" ${param.hora == '10:30' ? 'selected' : ''}>10:30 AM</option>
                            <option value="11:00" ${param.hora == '11:00' ? 'selected' : ''}>11:00 AM</option>
                            <option value="11:30" ${param.hora == '11:30' ? 'selected' : ''}>11:30 AM</option>
                            <option value="12:00" ${param.hora == '12:00' ? 'selected' : ''}>12:00 PM</option>
                            <option value="12:30" ${param.hora == '12:30' ? 'selected' : ''}>12:30 PM</option>
                            <option value="13:00" ${param.hora == '13:00' ? 'selected' : ''}>01:00 PM</option>
                            <option value="13:30" ${param.hora == '13:30' ? 'selected' : ''}>01:30 PM</option>
                            <option value="14:00" ${param.hora == '14:00' ? 'selected' : ''}>02:00 PM</option>
                            <option value="14:30" ${param.hora == '14:30' ? 'selected' : ''}>02:30 PM</option>
                            <option value="15:00" ${param.hora == '15:00' ? 'selected' : ''}>03:00 PM</option>
                            <option value="15:30" ${param.hora == '15:30' ? 'selected' : ''}>03:30 PM</option>
                            <option value="16:00" ${param.hora == '16:00' ? 'selected' : ''}>04:00 PM</option>
                            <option value="16:30" ${param.hora == '16:30' ? 'selected' : ''}>04:30 PM</option>
                            <option value="17:00" ${param.hora == '17:00' ? 'selected' : ''}>05:00 PM</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Motivo de la consulta</label>
                        <textarea name="motivo" rows="3" placeholder="Ej. Vacunacion o chequeo general">${param.motivo}</textarea>
                    </div>

                    <button type="submit" class="btn-primary">${empty param.id ? 'Agendar Cita' : 'Guardar Cambios'}</button>
                </form>

                <div class="action-footer">
                    <a href="CitaServlet" class="btn-return">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <line x1="19" y1="12" x2="5" y2="12"></line>
                            <polyline points="12 19 5 12 12 5"></polyline>
                        </svg>
                        Ver Listado de Citas
                    </a>
                </div>
            </div>
        </main>
    </body>
</html>
