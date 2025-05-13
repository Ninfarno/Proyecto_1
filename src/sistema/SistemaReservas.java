package sistema;

import modelo.*;

import static sistema.SistemaDeDatos.*;
import static sistema.SistemaScanner.*;

import java.util.*;

public class SistemaReservas {
    private List<Empleado> empleados;
    private List<Pasajero> pasajeros;
    private List<Vuelo> vuelos;
    private Map<String, List<Vuelo>> vuelosPorDia = new HashMap<>();

    public SistemaReservas() {
        empleados = new ArrayList<>();
        pasajeros = new ArrayList<>();
        vuelos = new ArrayList<>();

        cargarEmpleadosDesdeArchivo(this.empleados);

        cargarPasajerosDesdeArchivo(this.pasajeros);

        cargarVuelosDesdeArchivo(this.vuelos);

        for (Vuelo v : vuelos) {
            v.iniciarOcupacionAleatoria();
        }
    }

    public Empleado iniciarSesionEmpleado() {
        System.out.print("Usuario: ");
        String usuario = (String) Sc(String.class);
        System.out.print("Contraseña: ");
        String contrasena = (String) Sc(String.class);

        for (Empleado e : empleados) {
            if (e.iniciarSesion(usuario, contrasena)) {
                System.out.println("Bienvenido " + e.getNombre());
                return e;
            }
        }
        System.out.println("Credenciales incorrectas.");
        return null;
    }

    public void registrarEmpleado() {
        System.out.println("----------< Registro de Empleado >----------");
        System.out.print("Nombre: ");
        String nombre = (String) Sc(String.class);
        System.out.print("Usuario: ");
        String usuario = (String) Sc(String.class);
        System.out.print("Contraseña: ");
        String contrasena = (String) Sc(String.class);

        Empleado nuevo = new Empleado(nombre, usuario, contrasena);
        empleados.add(nuevo);

        guardarEmpleadoEnArchivo(nuevo);

        System.out.println("Empleado registrado exitosamente.");
    }

    public Pasajero iniciarSesionPasajero() {
        System.out.print("Usuario: ");
        String usuarioInput = ((String) Sc(String.class)).trim().toLowerCase(); // Normalizar usuario

        // Buscar si el usuario ya existe (ignorando mayúsculas/minúsculas)
        for (Pasajero p : pasajeros) {
            if (p.getUsuario().trim().toLowerCase().equals(usuarioInput)) {
                // Usuario encontrado, permitir hasta 3 intentos
                int intentos = 3;
                while (intentos > 0) {
                    System.out.print("Contraseña: ");
                    String contrasena = ((String) Sc(String.class)).trim();

                    if (p.iniciarSesion(p.getUsuario(), contrasena)) {
                        System.out.println("Bienvenido " + p.getNombre());
                        return p;
                    } else {
                        intentos--;
                        System.out.println("Contraseña incorrecta. Intentos restantes: " + intentos);
                    }
                }
                System.out.println("Demasiados intentos fallidos. Sesión cancelada.");
                return null;
            }
        }

        // Usuario no encontrado, proceder al registro
        System.out.println("Usuario no registrado. Procediendo al registro...");
        System.out.print("Nombre completo: ");
        String nombre = ((String) Sc(String.class)).trim();

        System.out.print("Cree una contraseña: ");
        String nuevaContrasena = ((String) Sc(String.class)).trim();

        Pasajero nuevo = new Pasajero(nombre, usuarioInput, nuevaContrasena);
        pasajeros.add(nuevo);
        guardarPasajeroEnArchivo(nuevo); // Asegúrate de que este método funcione correctamente

        System.out.println("Pasajero registrado exitosamente. \n\nBienvenido " + nombre);
        return nuevo;
    }


    public void menuEmpleado(Empleado empleado) {
        int opcion;
        do {
            System.out.println("\n--- Menú Empleado ---");
            System.out.println("1. Crear vuelo");
            System.out.println("2. Eliminar vuelo");
            System.out.println("3. Cancelar vuelo");
            System.out.println("4. Ver todos los vuelos");
            System.out.println("5. Agregar empleado");
            System.out.println("6. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

            opcion = (int) Sc(int.class);

            switch (opcion) {
                case 1 -> crearVuelo();
                case 2 -> eliminarVuelo();
                case 3 -> cancelarVuelo();
                case 4 -> mostrarVuelos();
                case 5 -> registrarEmpleado();
                case 6 -> {
                    System.out.println("Sesión cerrada.");
                    return;
                }
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 6);
    }

    // Crea el vuelo y lo agrega ordenadamente
    private void crearVuelo() {
        System.out.print("Id del Vuelo: ");
        String id = (String) Sc(String.class);

        System.out.print("Día del vuelo: ");
        String dia = (String) Sc(String.class);

        System.out.print("Origen: ");
        String origen = (String) Sc(String.class);

        System.out.print("Destino: ");
        String destino = (String) Sc(String.class);

        System.out.print("Capacidad total: ");
        int capacidad = (int) Sc(int.class);

        System.out.print("Hora de salida (ej. 08:00): ");
        String hora = (String) Sc(String.class);

        System.out.print("Precio del boleto: ");
        double precio = (double) Sc(double.class);

        Vuelo nuevoVuelo = new Vuelo(id, dia, origen, destino, capacidad, hora, precio);
        agregarVueloOrdenado(nuevoVuelo);
        guardarVueloEnArchivo(nuevoVuelo);

        vuelos.clear();
        cargarVuelosDesdeArchivo(vuelos);

        System.out.println("Vuelo creado exitosamente:\n" + nuevoVuelo);
    }

    private void eliminarVuelo() {
        mostrarVuelos();
        System.out.print("\nEscriba el Id del vuelo a eliminar: ");
        String id = (String) Sc(String.class);

        // Elimina el vuelo del archivo
        eliminarVueloDeArchivo(id, vuelos);

        // Cargar vuelos actualizados desde el archivo
        List<Vuelo> vuelosActualizados = new ArrayList<>();
        cargarVuelosDesdeArchivo(vuelosActualizados);

        // Actualiza la lista en memoria con los vuelos cargados sin limpiar 'vuelos'
        vuelos.clear();
        vuelos.addAll(vuelosActualizados);

        return;
    }


    public void agregarVueloOrdenado(Vuelo nuevoVuelo) {
        String diaNuevo = nuevoVuelo.getDia();
        for (int i = 0; i < vuelos.size(); i++) {
            String diaActual = vuelos.get(i).getDia();
            if (ordenDias.indexOf(diaNuevo) < ordenDias.indexOf(diaActual)) {
                vuelos.add(i, nuevoVuelo); // lo inserta justo antes del primero que tenga un día posterior
                return;
            }
        }
        vuelos.add(nuevoVuelo); // si no encontró lugar, lo agrega al final
    }

    private static final List<String> ordenDias = Arrays.asList(
            "Lunes", "Martes", "Miércoles", "Miercoles", "Jueves", "Viernes", "Sábado", "Sabado", "Domingo"
    );


    //Elimina los vueloso con ID
    private void cancelarVuelo() {
        if (vuelos.isEmpty()) {
            System.out.println("No hay vuelos para cancelar.");
            return;
        }

        mostrarVuelos();

        System.out.print("Ingrese ID del vuelo a cancelar: ");
        String id = (String) Sc(String.class);

        for (Vuelo v : vuelos) {
            if (v.getId().equals(id)) {
                if (v.isCancelado()) {
                    System.out.println("Este vuelo ya ha sido cancelado.");
                    return;
                }

                double porcentajeOcupacion = ((double) v.getOcupados() / v.getCapacidad()) * 100;

                if (porcentajeOcupacion >= 30.0) {
                    System.out.printf("El vuelo no puede ser cancelado porque está ocupado en un %.2f%%.%n", porcentajeOcupacion);
                    return;
                }

                System.out.print("Motivo de la cancelación: ");
                String motivo = (String) Sc(String.class);
                v.cancelar(motivo);
                System.out.println("Vuelo cancelado con motivo: " + motivo);
                actualizarArchivoDeVuelos(vuelos);
                return;
            }
        }

        System.out.println("ID de vuelo no encontrado.");
    }

    public void menuPasajero(Pasajero pasajero) {
        int opcion;
        do {
            System.out.println("\n--- Menú Pasajero ---");
            System.out.println("1. Ver vuelos disponibles");
            System.out.println("2. Buscar vuelos");
            System.out.println("3. Reservar vuelo");
            System.out.println("4. Ver Reservas");
            System.out.println("5. Cancelar vuelo");
            System.out.println("6. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            opcion = (int) Sc(int.class);

            switch (opcion) {
                case 1 -> mostrarVuelos();
                case 2 -> buscarVuelos(pasajero);
                case 3 -> reservarVuelo(pasajero, null);
                case 4 -> pasajero.verReservas();
                case 5 -> borrarVuelo(pasajero);
                case 6 -> System.out.println("Sesión cerrada.");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 6);
    }

    private void mostrarVuelos() {

        vuelosPorDia.clear();

        for (Vuelo vuelo : vuelos) {
            vuelosPorDia.computeIfAbsent(vuelo.getDia(), k -> new ArrayList<>()).add(vuelo);
        }

        if (vuelosPorDia.isEmpty()) {
            System.out.println("No hay vuelos registrados.");
            return;
        }

        for (String dia : vuelosPorDia.keySet()) {
            System.out.println("\n--- Vuelos para " + dia + " ---");

            List<Vuelo> vuelosDelDia = vuelosPorDia.get(dia);

            for (Vuelo vuelo : vuelosDelDia) {
                System.out.println(vuelo);
            }
        }
        SistemaScanner.Sc(EsperarEnter.class);
    }


    private void buscarVuelos(Pasajero pasajero) {
        System.out.print("Origen: ");
        String origen = (String) Sc(String.class);
        System.out.print("Destino: ");
        String destino = (String) Sc(String.class);
        List<Vuelo> coincidencias = new ArrayList<>();

        System.out.println("\n--- Resultados de búsqueda ---");

        for (Vuelo v : vuelos) {
            if (v.getOrigen().equalsIgnoreCase(origen) &&
                    v.getDestino().equalsIgnoreCase(destino) &&
                    !v.isCancelado()) {

                System.out.println(v);
                coincidencias.add(v);
            }
        }
        if (coincidencias.isEmpty()) {
            System.out.println("No se encontraron vuelos disponibles con ese origen y destino.");
            return;
        } else {
            boolean hayAsientos = coincidencias.stream().anyMatch(v -> v.getOcupados() < v.getCapacidad());

            if (!hayAsientos) {
                System.out.println("Todos los vuelos encontrados están llenos. Intente con otra fecha o destino.");
                return;
            }

            System.out.println("Desea reservar en alguno?\nSi/No: ");
            boolean Reservar = (boolean) Sc(boolean.class);

            if (Reservar) {
                System.out.print("Ingrese el ID del vuelo en el que desea reservar: ");
                String idSeleccionado = (String) Sc(String.class);

                for (Vuelo v : vuelos) {
                    if (v.getId().equals(idSeleccionado)) {
                        reservarVuelo(pasajero, idSeleccionado);
                        return;
                    }
                }
                System.out.println("ID de vuelo no válido.");
            }
        }
    }

    private void reservarVuelo(Pasajero pasajero, String id) {
        if (id == null || id.trim().isEmpty()) {
            mostrarVuelos();
            System.out.print("Ingrese ID del vuelo a reservar: ");
            id = (String) Sc(String.class);
        }

        for (Vuelo v : vuelos) {
            if (v.getId().equalsIgnoreCase(id)) {
                if (v.isCancelado()) {
                    System.out.println("El vuelo está cancelado.");
                    return;
                }

                if (v.getOcupados() >= v.getCapacidad()) {
                    System.out.println("El vuelo ya está lleno.");
                    return;
                }

                // Actualizar la matriz con base en las reservas existentes
                SistemaDeDatos.actualizarMatrizDesdeReservas(v);
                mostrarMatrizDeAsientos(v);

                System.out.print("¿Asientos a reservar? (ej: 1;3;5): ");
                String entrada = (String) Sc(String.class);
                String[] partes = entrada.split(";");

                List<Integer> seleccionados = new ArrayList<>();

                for (String s : partes) {
                    try {
                        int asiento = Integer.parseInt(s.trim());

                        if (asiento < 1 || asiento > v.getCapacidad()) {
                            System.out.println("Número de asiento inválido: " + asiento);
                            System.out.print("¿Desea reservar el resto? Si/No: ");
                            if ((boolean) Sc(boolean.class)) continue;
                            return;
                        }

                        // Verificar si el asiento ya fue reservado por otro pasajero
                        if (SistemaDeDatos.asientoYaReservado(v, asiento)) {
                            System.out.println("El asiento " + asiento + " ya fue reservado.");
                            System.out.print("¿Desea reservar el resto? Si/No: ");
                            if ((boolean) Sc(boolean.class)) continue;
                            return;
                        }

                        // Verificar si el asiento ya está ocupado en la matriz
                        if (v.getAsientos()[asiento - 1]) {
                            System.out.println("Asiento ya ocupado: " + asiento);
                            System.out.print("¿Desea reservar el resto? Si/No: ");
                            if ((boolean) Sc(boolean.class)) continue;
                            return;
                        }

                        seleccionados.add(asiento);
                    } catch (NumberFormatException e) {
                        System.out.println("Formato inválido: " + s);
                        return;
                    }
                }

                if (seleccionados.isEmpty()) {
                    System.out.println("No se seleccionaron asientos válidos.");
                    return;
                }

                Ticket ticket = v.reservar(pasajero, seleccionados);

                if (ticket != null) {
                    System.out.println("Reserva exitosa. Asientos reservados: " + seleccionados);
                    System.out.println("¿Desea confirmar reserva? Si/No:");
                    if ((boolean) Sc(boolean.class)) {
                        pasajero.agregarVueloTomado(v.getId(), seleccionados);
                        guardarPasajeroEnArchivo(pasajero);
                        SistemaDeDatos.guardarReservasEnArchivo(pasajero, v, seleccionados); // ✅ Guardar en reservas.txt
                        ticket.imprimir();
                    } else {
                        System.out.println("Reserva cancelada por el usuario.");
                    }
                } else {
                    System.out.println("No se pudo realizar la reserva.");
                }
                return;
            }
        }

        System.out.println("Vuelo no encontrado.");
    }




    private void borrarVuelo(Pasajero pasajero) {
        if (pasajero.getVuelosTomados().isEmpty()) {
            System.out.println("No tiene vuelos reservados para cancelar.");
            return;
        }

        pasajero.verReservas();
        System.out.print("Ingrese el ID del vuelo que desea cancelar: ");
        String id = (String) Sc(String.class);

        // Obtener el vuelo desde la lista global
        Vuelo vueloACancelar = null;
        for (Vuelo v : vuelos) {
            if (v.getId().equalsIgnoreCase(id)) {
                vueloACancelar = v;
                break;
            }
        }

        if (vueloACancelar == null) {
            System.out.println("No se encontró el vuelo con ese ID.");
            return;
        }

        // Cancelar desde el objeto pasajero
        pasajero.cancelarVuelo(id);

        // Guardar cambios en archivo de pasajeros
        guardarPasajeroEnArchivo(pasajero);

        // Eliminar la reserva del archivo de reservas
        SistemaDeDatos.eliminarReserva(pasajero, vueloACancelar);

        // (Opcional) Actualizar matriz de asientos en memoria
        SistemaDeDatos.actualizarMatrizDesdeReservas(vueloACancelar);

        System.out.println("Reserva cancelada correctamente.");
    }




    private void mostrarMatrizDeAsientos(Vuelo vuelo) {
        boolean[] asientos = vuelo.getAsientos();
        int columnas = 4;

        System.out.println("Asientos (XX = ocupado):");
        for (int i = 0; i < asientos.length; i++) {
            if (i % columnas == 0) System.out.println();
            if (i % (columnas/2) == 0) System.out.print("    ");
            System.out.print(asientos[i] ? "[XX] " : String.format("[%02d] ", i + 1));
        }
        System.out.println();
    }
}
