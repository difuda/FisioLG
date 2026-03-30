# Sistema de Gestión de Agendas - Centro de Fisioterapia

## 1. Descripción Técnica
Aplicación web diseñada para la automatización de la gestión de citas en entornos clínicos. El sistema implementa una arquitectura cliente-servidor para la persistencia de datos y la gestión dinámica de calendarios.

## 2. Stack Tecnológico
* **Backend:** Java 17, Framework Spring Boot (Spring Data JPA, Spring Security).
* **Base de Datos:** MySQL 8.0.
* **Frontend:** HTML5, CSS3 (Bootstrap 5), JavaScript (Vanilla).
* **Integraciones:** API de FullCalendar para la agenda técnica.

## 3. Requisitos Previos
* Java Development Kit (JDK) 17 o superior.
* Maven 3.8+.
* Instancia de MySQL Server operativa.

## 4. Instalación y Configuración

### 4.1. Configuración de la Base de Datos
1. Crear un esquema en MySQL llamado `gestion_fisioterapia`.
2. Ejecutar el script de creación de tablas.
3. Actualizar el archivo `application.properties` con las credenciales locales:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_fisioterapia
spring.datasource.username=USUARIO
spring.datasource.password=CONTRASEÑA
```

### 4.2. Compilación y Ejecución
Desde la raíz del proyecto, ejecutar el siguiente comando en la terminal para iniciar el servidor:

```bash
mvn spring-boot:run
```

La aplicación será accesible en la dirección local: `http://localhost:8080`

## 5. Funcionalidades de Ingeniería
* **Lógica de Citas:** Algoritmo de validación de disponibilidad en bloques de 40 minutos.
* **Gestión de Concurrencia:** Soporte para la monitorización de agendas duales simultáneas.
* **Control de Acceso:** Implementación de seguridad a nivel de métodos y rutas con Spring Security.
* **Persistencia:** Gestión de estados de pacientes y registros de tratamiento mediante JPA.

## 6. Autor
* **Diego** - Desarrollo de Software