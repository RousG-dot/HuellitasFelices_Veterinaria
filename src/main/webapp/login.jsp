<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login - Huellitas Felices</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    </head>
    <body>
        <main class="login-page">
            <section class="login-card" aria-labelledby="login-title">
                <div class="login-brand-panel">
                    <div class="login-brand-inner">
                        <img
                            src="${pageContext.request.contextPath}/img/fondo_pet.png"
                            alt="Logo de Huellitas Felices"
                            class="login-brand-logo">
                        <h1>Huellitas Felices</h1>
                        <p>Gestión veterinaria para quienes hacen tu vida más feliz.</p>
                    </div>
                </div>

                <div class="login-form-panel">
                    <div class="login-form-shell">
                        <header class="login-form-header">
                            <h2 id="login-title">Bienvenido</h2>
                            <p>Ingresa al panel administrativo</p>
                        </header>

                        <c:if test="${not empty error}">
                            <div class="login-error" role="alert" aria-live="polite">
                                <p><c:out value="${error}" /></p>
                            </div>
                        </c:if>

                        <form method="POST" action="${pageContext.request.contextPath}/LoginServlet" class="login-form">
                            <div class="form-group">
                                <label for="txtemail">Correo electrónico</label>
                                <input
                                    type="email"
                                    id="txtemail"
                                    name="txtemail"
                                    value="${fn:escapeXml(correoIngresado)}"
                                    placeholder="usuario@huellitasfelices.com"
                                    autocomplete="username"
                                    required>
                            </div>

                            <div class="form-group">
                                <label for="txtpass">Contraseña</label>
                                <div class="password-field">
                                    <input
                                        type="password"
                                        id="txtpass"
                                        name="txtpass"
                                        placeholder="Ingresa tu contraseña"
                                        autocomplete="current-password"
                                        required>
                                    <button
                                        type="button"
                                        class="password-toggle"
                                        aria-label="Mostrar contraseña"
                                        aria-controls="txtpass"
                                        aria-pressed="false">
                                        <span class="password-toggle-icon password-toggle-icon-show" aria-hidden="true">
                                            <svg viewBox="0 0 24 24" focusable="false">
                                                <path d="M12 5C6.5 5 2.1 8.3.3 12c1.8 3.7 6.2 7 11.7 7s9.9-3.3 11.7-7C21.9 8.3 17.5 5 12 5zm0 11.2A4.2 4.2 0 1 1 12 7.8a4.2 4.2 0 0 1 0 8.4zm0-6.6A2.4 2.4 0 1 0 12 14a2.4 2.4 0 0 0 0-4.8z"/>
                                            </svg>
                                        </span>
                                        <span class="password-toggle-icon password-toggle-icon-hide" aria-hidden="true">
                                            <svg viewBox="0 0 24 24" focusable="false">
                                                <path d="m3.3 2 18.7 18.7-1.3 1.3-3.1-3.1A13.5 13.5 0 0 1 12 20c-5.5 0-9.9-3.3-11.7-7a15 15 0 0 1 4.6-5.4L2 3.3 3.3 2zm8.7 5.8a4.2 4.2 0 0 1 4.2 4.2c0 .8-.2 1.5-.6 2.1l-5.7-5.7c.6-.4 1.3-.6 2.1-.6zm0-2.8c5.5 0 9.9 3.3 11.7 7a15 15 0 0 1-4.8 5.5l-2-2a6 6 0 0 0 .9-3.2 5.8 5.8 0 0 0-5.8-5.8c-1.2 0-2.3.3-3.2 1l-2-2A13.3 13.3 0 0 1 12 5z"/>
                                            </svg>
                                        </span>
                                    </button>
                                </div>
                            </div>

                            <button type="submit" class="login-submit">Ingresar al sistema</button>
                        </form>
                    </div>
                </div>
            </section>
        </main>

        <script>
            (function () {
                var toggleButton = document.querySelector('.password-toggle');
                var passwordInput = document.getElementById('txtpass');

                if (!toggleButton || !passwordInput) {
                    return;
                }

                toggleButton.addEventListener('click', function () {
                    var isVisible = passwordInput.type === 'text';
                    passwordInput.type = isVisible ? 'password' : 'text';
                    toggleButton.setAttribute('aria-label', isVisible ? 'Mostrar contraseña' : 'Ocultar contraseña');
                    toggleButton.setAttribute('aria-pressed', String(!isVisible));
                    toggleButton.classList.toggle('is-visible', !isVisible);
                });
            }());
        </script>
    </body>
</html>
