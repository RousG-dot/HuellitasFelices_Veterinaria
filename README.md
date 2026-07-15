# Huellitas Felices - Sistema Web de Gestion Veterinaria

## Descripcion general

Huellitas Felices es un proyecto academico de gestion veterinaria orientado a un panel administrativo web. La aplicacion permite iniciar sesion, acceder a un dashboard con resumenes del sistema y gestionar clientes, mascotas, citas e inventario de productos.

El proyecto fue construido con Java Web sobre Apache Tomcat y utiliza JDBC para conectarse a MySQL o MariaDB. La interfaz combina JSP/JSTL para formularios y listados, y JSF para el dashboard principal.

## Funcionalidades

- Autenticacion mediante correo y contrasena.
- Cierre de sesion real con invalidacion de sesion.
- Proteccion de rutas mediante filtro de autenticacion.
- Dashboard con contadores de clientes, mascotas, productos y citas.
- Gestion de Clientes.
- Gestion de Mascotas.
- Gestion de Citas.
- Gestion de Inventario o Productos.

## Seguridad implementada

- `AuthFilter` protege las rutas privadas y redirige al login cuando no existe sesion valida.
- La sesion autenticada se controla con el atributo `usuarioLogueado`.
- `LogoutServlet` invalida la sesion y devuelve al formulario de acceso.
- Las contrasenas de usuarios se validan con BCrypt mediante `org.mindrot:jbcrypt:0.4`.
- El login muestra mensajes genericos de error y no expone detalles internos.
- Las validaciones principales se realizan en backend en servlets y servicios segun el modulo.

## Arquitectura

La aplicacion sigue una estructura web por capas:

`Vista -> Servlet -> Service -> DAO -> Base de datos`

- Las vistas se renderizan con JSP/JSTL y JSF.
- Los `Servlet` controlan el flujo HTTP, la navegacion y los mensajes hacia la vista.
- Algunos modulos incorporan capa `Service` cuando concentran reglas de negocio y validaciones reutilizables, como `ProductoService`, `MascotaService` y `CitaService`.
- Los `DAO` trabajan con JDBC directo sobre MySQL o MariaDB.
- No existe una API REST en este proyecto.
- No existe una capa JPA activa; la persistencia actual se realiza con JDBC.

## Tecnologias

| Tecnologia | Uso |
|---|---|
| JDK 22.0.1 | Entorno Java instalado en el equipo de desarrollo |
| Java 17 (`maven.compiler.release`) | Nivel real de compilacion configurado en Maven |
| Maven 3.9.6 | Construccion del proyecto y gestion de dependencias |
| Jakarta Servlet 6 | Controladores HTTP y filtro de autenticacion |
| JSF 4 | Dashboard principal |
| JSP y JSTL | Formularios, listados y vistas del sistema |
| Weld 5 | Integracion CDI para el entorno web |
| JDBC | Acceso a datos |
| MySQL o MariaDB | Motor de base de datos |
| BCrypt (`org.mindrot:jbcrypt:0.4`) | Verificacion segura de contrasenas |
| Apache Tomcat 10.1.53 | Contenedor de despliegue |
| NetBeans 22 | Entorno recomendado para abrir y ejecutar el proyecto |

## Estructura del proyecto

```text
src/main/java       codigo fuente Java (servlets, services, dao, model, dto, filter, bean, util)
src/main/webapp     vistas JSP, dashboard JSF, recursos web y WEB-INF
src/main/resources  recursos de compilacion
database            script SQL de creacion y carga inicial
pom.xml             configuracion Maven del proyecto
README.md           documentacion principal
```

## Requisitos previos

- JDK 22 o una instalacion compatible para el entorno local.
- Maven 3.9.x o compatible.
- Apache Tomcat 10.1.x.
- MySQL o MariaDB.
- NetBeans 22 como opcion recomendada para desarrollo y despliegue local.

## Configuracion de la base de datos

1. Crear una base de datos nueva o controlada para el proyecto.
2. Importar el archivo `database/db_veterinaria.sql`.
3. Revisar la configuracion JDBC en `src/main/java/com/pe/vet/veterinaria/util/Conexion.java`.
4. Confirmar en ese archivo el nombre de la base, el puerto, el usuario y la contrasena que se usaran localmente.

Datos confirmados en el codigo actual:

- Base de datos: `db_veterinaria`
- URL JDBC con formato: `jdbc:mysql://<host>:<puerto>/db_veterinaria`
- Puerto configurado actualmente en el codigo: `3307`

Advertencia:

El script SQL puede contener sentencias `DROP TABLE`. Debe utilizarse solo para crear una base nueva o un entorno controlado, no sobre una base con datos importantes.

## Configuracion de la contrasena del administrador

El usuario administrador se autentica con BCrypt. El script SQL ya incluye un hash listo para la cuenta academica de acceso.

Credencial de prueba confirmada en el proyecto:

- Correo: `admin@petsociety.com`
- Contrasena: `admin123`

Consideraciones:

- La contrasena no se guarda en texto plano en la base de datos.
- Para una instalacion nueva, el script ya contiene el hash necesario.
- Si se reutiliza una base anterior, el registro del usuario debe estar alineado con el hash BCrypt esperado por el sistema.

## Compilacion

Comando principal de compilacion:

```bash
mvn clean package
```

Alternativa desde NetBeans:

- Usar `Clean and Build`.

Alternativa opcional si se desea usar el Maven incluido en NetBeans:

```text
"<RUTA_DE_NETBEANS>/netbeans/java/maven/bin/mvn.cmd" clean package
```

La ruta exacta depende de la instalacion local y no es necesaria si ya se dispone de Maven en el sistema.

El archivo WAR se genera en:

```text
target/
```

Si `clean` falla porque Tomcat mantiene archivos bloqueados, especialmente relacionados con Weld, se debe detener Tomcat antes de volver a compilar.

## Despliegue en Tomcat

1. Detener Tomcat si ya estaba ejecutandose.
2. Ejecutar `clean package` o `Clean and Build` desde el IDE.
3. Ubicar el WAR generado en `target/`.
4. Desplegarlo en Tomcat.
5. Iniciar Tomcat.
6. Abrir la aplicacion en el contexto correspondiente.
7. Probar el login y navegar por los modulos principales.

Artefacto y WAR confirmados:

- `artifactId`: `veterinaria`
- WAR generado: `veterinaria-1.0-SNAPSHOT.war`

Ejemplo conceptual de acceso:

```text
http://localhost:8080/<contexto>
```

Segun el metodo de despliegue, el contexto suele derivarse del nombre del WAR o de la configuracion del servidor.

## Flujo basico de uso

1. Iniciar sesion con un usuario valido.
2. Revisar el dashboard principal.
3. Registrar o actualizar clientes.
4. Registrar o actualizar mascotas asociadas a un cliente.
5. Registrar citas validando la relacion entre cliente y mascota.
6. Gestionar productos del inventario.
7. Cerrar sesion.

## Integridad de datos

- Cada mascota puede estar relacionada con un cliente mediante `cliente_id`.
- Cada cita conserva relacion con cliente y mascota mediante `cliente_id` y `mascota_id`.
- El sistema valida que la mascota seleccionada pertenezca al cliente seleccionado al registrar o actualizar citas.
- La tabla `citas` tambien conserva campos textuales como `cliente` y `mascota`, lo que ayuda a mantener referencia historica visible.
- Las claves foraneas confirmadas en el script usan `ON DELETE SET NULL` donde corresponde para evitar perdida total de trazabilidad relacional.

## Validaciones principales

- Validacion de correo y contrasena en el login.
- Validacion de IDs positivos y existentes en operaciones de edicion o eliminacion.
- Validacion de campos obligatorios en clientes, mascotas, citas y productos.
- Validacion de existencia de cliente antes de registrar o actualizar mascotas.
- Validacion de existencia y correspondencia entre cliente y mascota en citas.
- Validacion de `precio > 0` en productos.
- Validacion de `stock >= 0` en productos.
- Mensajes controlados para errores de validacion y errores tecnicos comunes.

## Pruebas realizadas

Se han realizado las siguientes verificaciones sobre el proyecto:

- Compilacion Maven con `BUILD SUCCESS`.
- Pruebas manuales de login.
- Pruebas manuales de logout.
- Pruebas manuales de dashboard.
- Pruebas manuales de CRUD de Clientes.
- Pruebas manuales de CRUD de Mascotas.
- Pruebas manuales de gestion de Citas e integridad entre cliente y mascota.
- Pruebas manuales de Inventario o Productos.
- Verificacion manual de validaciones principales.

El proyecto no cuenta actualmente con una suite automatizada de pruebas.

## Limitaciones conocidas

- La configuracion JDBC sigue definida localmente dentro de `Conexion.java`, por lo que debe ajustarse segun el entorno.
- El despliegue esta planteado para ejecucion local sobre Tomcat.
- El proyecto no cuenta actualmente con pruebas automatizadas.
- La tabla de citas mantiene algunas decisiones heredadas del esquema, como columnas textuales complementarias para cliente y mascota ademas de las claves foraneas.

## Contexto academico

Este repositorio corresponde a un proyecto academico de aplicacion web veterinaria desarrollado en Java Web para fines de aprendizaje, mantenimiento y evaluacion tecnica.
