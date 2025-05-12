P R O Y E C T O   1  ! 

Integrantes:
- Luis Mendoza Madriz
- Carlos Ayala Mendoza
- Luis Felipe Delgado Morales
- Jose Armando Leal Rodriguez

# Patito Volador - Sistema de Reservas de Vuelos

Este proyecto es un sistema de gestión de vuelos para la aerolínea ficticia **Patito Volador**, desarrollado en **Java** con almacenamiento en archivos de texto. Permite a los pasajeros y empleados interactuar con el sistema para gestionar vuelos, hacer reservas, iniciar sesión, registrar nuevos usuarios y cancelar vuelos.

---

## Características principales

- **Inicio de sesión de pasajeros** con verificación de usuario y contraseña.
- **Registro automático** si el usuario no existe.
- **Persistencia de datos** usando archivos `.txt`.
- **Visualización y búsqueda de vuelos** por origen y destino.
- **Reservas de vuelos** para pasajeros.
- **Cancelación de vuelos** con registro del motivo.
- **Gestión de vuelos** por parte del personal autorizado (empleados).
- **Control de intentos de login** (hasta 3 intentos de contraseña).

---

## Estructura del Proyecto

---

## Requisitos

- Java 8 o superior
- IDE recomendado: IntelliJ IDEA, Eclipse o VS Code con soporte Java
- Crear un directorio `Datos/` en la raíz para almacenar los archivos `.txt`

---

## Cómo ejecutar el proyecto

1. Clona el repositorio o copia el proyecto a tu equipo.
2. Asegúrate de tener la estructura de carpetas correcta (`Datos/`).
3. Ejecuta la clase `Main.java` desde tu IDE o consola.
4. Interactúa con el menú para registrar o iniciar sesión como pasajero o empleado.

---

## Detalles del sistema

### Login y Registro de Pasajeros

- El pasajero escribe su **usuario**.
- Si existe, se solicitan hasta 3 intentos de **contraseña**.
- Si no existe, el sistema solicita su nombre y una nueva contraseña para registrar automáticamente.

### Guardado de Información

- Toda la información de pasajeros se guarda en `Datos/pasajeros.txt`.
- Cada línea sigue el formato:
  
