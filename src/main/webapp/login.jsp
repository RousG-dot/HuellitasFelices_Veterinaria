<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login - Huellitas Felices</title>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">

    </head>
    <body>
        
        <div class="login-container">
            <div class="header-section">
                <img src="img/logo_hf.png" alt="PetSociety Logo"/>
                <h1>Huellitas Felices</h1>   
                <h2>Panel Administrativo</h2> 
            </div>

            <c:if test="${not empty error}">
                <p class="error-message"><c:out value="${error}" /></p>
            </c:if>
            
            <form method="POST" action="${pageContext.request.contextPath}/LoginServlet">
                <div class="form-group">
                    <label>Correo Electrónico</label>
                    <input type="email" name="txtemail" value="${fn:escapeXml(correoIngresado)}" placeholder="usuario@petsociety.com" required>
                </div>
                
                <div class="form-group">
                    <label>Contraseña</label>
                    <input type="password" name="txtpass" placeholder="••••••••" required>
                </div>
                
                <button type="submit">Ingresar al Sistema</button>
            </form>
        </div>
        
    </body>
</html>
