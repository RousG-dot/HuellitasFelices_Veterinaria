# Huellitas Felices

## Sistema Web de Gestión Veterinaria

Huellitas Felices es un proyecto académico de gestión veterinaria orientado a un panel administrativo web. Permite iniciar sesión, consultar un dashboard y gestionar clientes, mascotas, citas e inventario de productos.

La aplicación fue desarrollada con Java Web sobre Apache Tomcat. Utiliza JDBC para conectarse a una base de datos MySQL o MariaDB. Las vistas combinan JSP y JSTL para formularios y listados, mientras que el dashboard principal utiliza JSF.

---

## Funcionalidades principales

- Inicio de sesión mediante correo y contraseña.
- Cierre de sesión con invalidación de sesión.
- Protección de rutas mediante filtro de autenticación.
- Dashboard con resumen de clientes, mascotas, citas y productos.
- Registro, edición, consulta y eliminación de clientes.
- Registro, edición, consulta y eliminación de mascotas.
- Asociación de mascotas con sus respectivos dueños.
- Registro y consulta de citas.
- Validación de la relación entre cliente y mascota.
- Gestión de productos del inventario.
- Control de disponibilidad y stock de productos.

---

## Seguridad implementada

- `AuthFilter` protege las rutas privadas.
- La sesión autenticada utiliza el atributo `usuarioLogueado`.
- `LogoutServlet` invalida la sesión activa.
- Las contraseñas se verifican con BCrypt.
- El login utiliza mensajes de error controlados.
- Las validaciones principales se ejecutan en backend.
- Las consultas a la base de datos utilizan JDBC y sentencias preparadas donde corresponde.

---

## Arquitectura

La aplicación sigue una estructura web por capas:

```text
Vista → Servlet → Service → DAO → Base de datos
```

### Vista

Las vistas se implementan con:

- JSP
- JSTL
- JSF
- HTML
- CSS
- JavaScript

### Servlet

Los Servlet controlan:

- solicitudes HTTP;
- navegación;
- validaciones iniciales;
- comunicación con las capas de servicio;
- mensajes enviados a las vistas.

### Service

Algunos módulos utilizan una capa de servicio para centralizar reglas de negocio y validaciones reutilizables.

Entre ellos:

- `ProductoService`
- `MascotaService`
- `CitaService`

### DAO

Los DAO realizan las operaciones de persistencia mediante JDBC.

La aplicación no utiliza una API REST ni una capa JPA activa.

---

## Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java 17 | Versión de compilación configurada en Maven |
| JDK 22.0.1 | Entorno utilizado durante el desarrollo |
| Maven 3.9.6 | Construcción y gestión de dependencias |
| Jakarta Servlet 6 | Controladores HTTP y filtro de autenticación |
| JSF 4 | Dashboard principal |
| JSP y JSTL | Formularios, listados y vistas |
| Weld 5 | Integración CDI |
| JDBC | Acceso a datos |
| MySQL o MariaDB | Base de datos |
| BCrypt | Protección de contraseñas |
| Apache Tomcat 10.1 | Servidor de aplicaciones |
| NetBeans 22 | IDE utilizado durante el desarrollo |

---

## Estructura del proyecto

```text
Veterinaria_DesarrolloWeb/
├── database/
│   └── db_veterinaria.sql
├── src/
│   └── main/
│       ├── java/
│       │   └── com/pe/vet/veterinaria/
│       ├── resources/
│       └── webapp/
├── pom.xml
├── README.md
└── .gitignore
```

### Directorios principales

```text
src/main/java
```

Contiene:

- Servlet
- Service
- DAO
- Model
- DTO
- Filter
- Bean
- Utilidades

```text
src/main/webapp
```

Contiene:

- vistas JSP;
- dashboard JSF;
- archivos CSS;
- imágenes;
- configuración web;
- recursos de interfaz.

```text
database
```

Contiene el script necesario para crear la base de datos desde cero.

---

# Requisitos previos

Antes de ejecutar el proyecto, se debe instalar:

- JDK 17 o superior.
- Apache Maven 3.9 o compatible.
- Apache Tomcat 10.1.
- MySQL 8 o MariaDB compatible.
- MySQL Workbench, DBeaver u otra herramienta de administración.
- NetBeans 22, IntelliJ IDEA o Eclipse.

La opción recomendada para este proyecto es NetBeans 22 con Tomcat 10.1.

---

# Configuración de la base de datos

## 1. Iniciar MySQL

Verificar que el servicio de MySQL o MariaDB esté encendido.

## 2. Abrir el script

Abrir el archivo:

```text
database/db_veterinaria.sql
```

Puede ejecutarse desde MySQL Workbench utilizando:

```text
File → Open SQL Script
```

## 3. Ejecutar el script completo

Seleccionar todo el contenido y ejecutarlo.

El script realiza las siguientes acciones:

- crea la base de datos `db_veterinaria`;
- crea las tablas;
- configura claves primarias y foráneas;
- inserta el usuario administrador;
- inserta datos ficticios de demostración.

## 4. Verificar las tablas

Ejecutar:

```sql
USE db_veterinaria;

SHOW TABLES;
```

Deben aparecer:

```text
citas
clientes
mascotas
productos
usuarios
```

## Advertencia

El script contiene instrucciones para eliminar y volver a crear las tablas.

Debe ejecutarse únicamente sobre una instalación nueva o una base de datos de prueba. No debe utilizarse sobre una base que contenga información importante sin realizar previamente un respaldo.

---

# Configuración de la conexión JDBC

Abrir el archivo:

```text
src/main/java/com/pe/vet/veterinaria/util/Conexion.java
```

Revisar los valores de conexión:

```java
jdbc:mysql://localhost:PUERTO/db_veterinaria
```

También deben configurarse:

- host;
- puerto;
- nombre de la base;
- usuario;
- contraseña.

Ejemplo habitual con MySQL:

```text
Host: localhost
Puerto: 3306
Base de datos: db_veterinaria
Usuario: root
Contraseña: contraseña local de MySQL
```

En algunos equipos el puerto puede ser diferente, por ejemplo `3307`.

El puerto configurado en `Conexion.java` debe coincidir con el puerto utilizado por MySQL en la computadora donde se ejecutará el proyecto.

No es necesario modificar ninguna otra clase para configurar la base de datos.

---

# Credenciales de acceso

El script SQL incluye un usuario administrador inicial.

```text
Correo: admin@huellitasfelices.com
Contraseña: admin123
```

La contraseña no se almacena en texto plano. La base de datos contiene únicamente su hash BCrypt.

Estas credenciales se incluyen exclusivamente para fines académicos y de evaluación.

---

# Compilación del proyecto

Desde la carpeta raíz ejecutar:

```bash
mvn clean package
```

Resultado esperado:

```text
BUILD SUCCESS
```

El archivo generado estará en:

```text
target/veterinaria-1.0-SNAPSHOT.war
```

## Compilación desde NetBeans

También puede utilizarse:

```text
Clean and Build
```

Si la limpieza falla porque Tomcat mantiene archivos bloqueados, se debe detener Tomcat y volver a ejecutar la compilación.

---

# Ejecución desde NetBeans

1. Abrir NetBeans.
2. Seleccionar:

   ```text
   File → Open Project
   ```

3. Abrir la carpeta del proyecto.
4. Configurar JDK 17 o superior.
5. Agregar Apache Tomcat 10.1 como servidor.
6. Confirmar que MySQL esté encendido.
7. Confirmar que la base `db_veterinaria` exista.
8. Revisar `Conexion.java`.
9. Ejecutar:

   ```text
   Clean and Build
   ```

10. Ejecutar el proyecto con:

   ```text
   Run
   ```

11. Abrir la URL indicada por NetBeans.

En la instalación utilizada durante el desarrollo, el contexto fue:

```text
http://localhost:8080/veterinaria/
```

El contexto puede variar dependiendo de la configuración del servidor y del nombre asignado al despliegue.

---

# Despliegue manual en Tomcat

1. Detener Tomcat si está en ejecución.
2. Compilar:

```bash
mvn clean package
```

3. Ubicar:

```text
target/veterinaria-1.0-SNAPSHOT.war
```

4. Copiar el WAR a:

```text
apache-tomcat-10.1.x/webapps/
```

5. Iniciar Tomcat.
6. Esperar a que Tomcat despliegue la aplicación.
7. Abrir en el navegador:

```text
http://localhost:8080/veterinaria-1.0-SNAPSHOT/
```

El nombre del contexto puede variar si el WAR es renombrado o si el servidor utiliza una configuración personalizada.

---

# Flujo básico de prueba

Para verificar la instalación:

1. Iniciar sesión con la cuenta administradora.
2. Confirmar que el dashboard cargue correctamente.
3. Abrir la sección Clientes.
4. Registrar un cliente.
5. Abrir la sección Mascotas.
6. Registrar una mascota y asociarla con un cliente.
7. Abrir la sección Citas.
8. Registrar una cita para un cliente y su mascota.
9. Abrir Inventario.
10. Registrar o editar un producto.
11. Cerrar sesión.

No deben aparecer errores HTTP `404` ni `500`.

---

# Integridad de datos

- Cada mascota puede relacionarse con un cliente mediante `cliente_id`.
- Cada cita puede relacionarse con un cliente mediante `cliente_id`.
- Cada cita puede relacionarse con una mascota mediante `mascota_id`.
- El sistema valida que la mascota seleccionada pertenezca al cliente indicado.
- La tabla `citas` conserva campos textuales para mantener una referencia visible.
- Las claves foráneas utilizan `ON DELETE SET NULL` donde corresponde.
- La eliminación de un cliente no elimina automáticamente toda la información histórica relacionada.

---

# Validaciones principales

## Login

- correo obligatorio;
- contraseña obligatoria;
- verificación mediante BCrypt;
- mensajes de error controlados.

## Clientes

- campos obligatorios;
- validación de identificadores;
- validación de datos duplicados cuando corresponde.

## Mascotas

- nombre obligatorio;
- cliente válido;
- asociación con dueño existente;
- validación de identificadores.

## Citas

- cliente obligatorio;
- mascota obligatoria;
- fecha y hora obligatorias;
- validación de la relación entre cliente y mascota.

## Productos

- nombre obligatorio;
- precio mayor que cero;
- stock mayor o igual que cero;
- control del estado disponible o no disponible.

---

# Pruebas realizadas

Se realizaron pruebas manuales de:

- inicio de sesión;
- cierre de sesión;
- protección de rutas;
- dashboard;
- gestión de clientes;
- gestión de mascotas;
- gestión de citas;
- relación entre cliente y mascota;
- gestión de productos;
- disponibilidad de productos;
- validaciones principales;
- navegación entre módulos;
- compilación Maven.

La compilación finalizó correctamente con:

```text
BUILD SUCCESS
```

Actualmente, el proyecto no incluye una suite automatizada de pruebas.

---

# Limitaciones conocidas

- La configuración JDBC debe ajustarse manualmente en `Conexion.java`.
- La ejecución está orientada a un entorno local con Tomcat.
- No existe una API REST.
- No existe persistencia activa mediante JPA.
- No hay pruebas automatizadas.
- El contexto de despliegue puede variar según la configuración de Tomcat.
- La tabla `citas` conserva columnas textuales además de claves foráneas por compatibilidad con la estructura heredada.

---

# Solución de problemas

## Error de conexión a la base de datos

Verificar:

- MySQL está encendido;
- el host es correcto;
- el puerto es correcto;
- la base `db_veterinaria` existe;
- el usuario y la contraseña son correctos;
- `Conexion.java` coincide con la configuración local.

## Error 404

Verificar:

- Tomcat está iniciado;
- el WAR fue desplegado;
- la URL utiliza el contexto correcto;
- no se está accediendo a una ruta inexistente.

## Error 500

Revisar:

- consola de Tomcat;
- conexión JDBC;
- estructura de las tablas;
- dependencias Maven;
- versión de Java.

## Error al ejecutar `mvn clean`

Detener Tomcat y volver a ejecutar:

```bash
mvn clean package
```

---

# Contexto académico

Este repositorio corresponde a un proyecto académico de aplicación web veterinaria desarrollado con Java Web para fines de aprendizaje, mantenimiento y evaluación técnica.
