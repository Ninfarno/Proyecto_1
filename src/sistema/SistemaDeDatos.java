package sistema;

import modelo.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class SistemaDeDatos {

    private List<Empleado> empleados = new ArrayList<>();

    // Base de datos para empleados
    public static void cargarEmpleadosDesdeArchivo(List<Empleado> empleados) {
        try (BufferedReader br = new BufferedReader(new FileReader("Datos/empleados.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 3) {
                    String nombre = partes[0].trim();
                    String usuario = partes[1].trim();
                    String contrasena = partes[2].trim();
                    empleados.add(new Empleado(nombre, usuario, contrasena));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de empleados: " + e.getMessage());
        }
    }
    public static void guardarEmpleadoEnArchivo(Empleado e) {
        try (FileWriter fw = new FileWriter("Datos/empleados.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(e.getNombre() + ";" + e.getUsuario() + ";" + e.getContrasena());
            bw.newLine();
        } catch (IOException ex) {
            System.out.println("Error al guardar el empleado: " + ex.getMessage());
        }
    }

    //Base de datos para pasajeros
    public static void cargarPasajerosDesdeArchivo(List<Pasajero> pasajeros) {
        try (BufferedReader br = new BufferedReader(new FileReader("Datos/pasajeros.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    String nombre = partes[0].trim();
                    String usuario = partes[1].trim();
                    String contrasena = partes[2].trim();

                    Pasajero pasajero = new Pasajero(nombre, usuario, contrasena);

                    if (partes.length > 3) {
                        pasajero.setVuelosTomadosDesdeTexto(partes);
                    }

                    pasajeros.add(pasajero);
                } else {
                    System.out.println("Formato incorrecto en la línea de pasajero: " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de pasajeros: " + e.getMessage());
        }
    }

    public static void guardarPasajeroEnArchivo(Pasajero nuevoPasajero) {
        List<Pasajero> pasajeros = new ArrayList<>();
        cargarPasajerosDesdeArchivo(pasajeros);

        boolean actualizado = false;

        for (int i = 0; i < pasajeros.size(); i++) {
            if (pasajeros.get(i).getUsuario().equalsIgnoreCase(nuevoPasajero.getUsuario())) {
                pasajeros.set(i, nuevoPasajero); // Actualiza el pasajero
                actualizado = true;
                break;
            }
        }

        if (!actualizado) {
            pasajeros.add(nuevoPasajero); // Nuevo pasajero
        }

        // Sobrescribe el archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Datos/pasajeros.txt", false))) {
            for (Pasajero p : pasajeros) {
                bw.write(p.obtenerLineaArchivo());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar pasajeros: " + e.getMessage());
        }
    }




    // Base de datos para vuelos
    public static void cargarVuelosDesdeArchivo(List<Vuelo> vuelos) {
        try (BufferedReader br = new BufferedReader(new FileReader("Datos/vuelos.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 6 && (partes[0].equals("ONLINE") || partes[0].equals("OFFLINE"))) {
                    String estado = partes[0].trim();
                    String id = partes[1].trim();
                    String dia = partes[2].trim();
                    String origen = partes[3].trim();
                    String destino = partes[4].trim();
                    int capacidad = Integer.parseInt(partes[5].trim());
                    String hora = partes[6].trim();
                    double precio = Double.parseDouble(partes[7].trim());

                    Vuelo vuelo = new Vuelo(id, dia, origen, destino, capacidad, hora, precio);

                    if (estado.equals("OFFLINE")) {
                        vuelo.cancelar();  // Marca el vuelo como cancelado
                    }

                    vuelos.add(vuelo);
                } else {
                    System.out.println("Formato incorrecto en la línea de vuelo: " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de vuelos: " + e.getMessage());
        }
    }
    public static void guardarVueloEnArchivo(Vuelo vuelo) {
        try (FileWriter fw = new FileWriter("Datos/vuelos.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            String estado = vuelo.isCancelado() ? "OFFLINE" : "ONLINE";
            bw.write(estado + ";" + vuelo.getId() + ";" + vuelo.getDia() + ";" + vuelo.getOrigen() + ";" +
                    vuelo.getDestino() + ";" + vuelo.getCapacidad() + ";" + vuelo.getHora() + ";" + vuelo.getPrecio());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar el vuelo en el archivo: " + e.getMessage());
        }
    }

    //Añadido esta parte para que guarde las reservas de los pasajeros
    public static void guardarReservasEnArchivo(Pasajero pasajero, Vuelo vuelo, List<Integer> nuevosAsientos) {
        File archivo = new File("Datos/reservas.txt");
        List<String> nuevasLineas = new ArrayList<>();
        String nuevaLinea = "";
        boolean actualizado = false;

        String origenDestino = vuelo.getOrigen() + " -> " + vuelo.getDestino();

        if (!archivo.exists()) {
            try {
                archivo.getParentFile().mkdirs();
                archivo.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo de reservas: " + e.getMessage());
                return;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    String nombre = partes[0];
                    String vueloInfo = partes[1];

                    if (nombre.equalsIgnoreCase(pasajero.getNombre()) &&
                            vueloInfo.equalsIgnoreCase(origenDestino)) {
                        // Ya existe una reserva, actualizamos los asientos
                        Set<Integer> todosAsientos = new HashSet<>();
                        for (int i = 2; i < partes.length; i++) {
                            todosAsientos.add(Integer.parseInt(partes[i]));
                        }
                        todosAsientos.addAll(nuevosAsientos); // Agrega nuevos sin duplicar

                        String asientosFinal = todosAsientos.stream()
                                .sorted()
                                .map(String::valueOf)
                                .collect(Collectors.joining(";"));

                        nuevaLinea = nombre + ";" + vueloInfo + ";" + asientosFinal;
                        nuevasLineas.add(nuevaLinea);
                        actualizado = true;
                    } else {
                        nuevasLineas.add(linea);
                    }
                } else {
                    nuevasLineas.add(linea); // línea inválida pero la conservamos por seguridad
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de reservas: " + e.getMessage());
        }

        if (!actualizado) {
            // No había entrada previa para este pasajero y vuelo
            nuevaLinea = pasajero.getNombre() + ";" + origenDestino + ";" +
                    nuevosAsientos.stream().map(String::valueOf).collect(Collectors.joining(";"));
            nuevasLineas.add(nuevaLinea);
        }

        // Escribimos el contenido actualizado
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (String linea : nuevasLineas) {
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo de reservas: " + e.getMessage());
        }
    }


    public static boolean asientoYaReservado(Vuelo vuelo, int asiento) {
        File archivo = new File("Datos/reservas.txt");

        if (!archivo.exists()) return false; // Si no hay reservas, nadie ha reservado

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    String origenDestino = partes[1];
                    String asientosStr = partes[2];

                    if (origenDestino.equalsIgnoreCase(vuelo.getOrigen() + " -> " + vuelo.getDestino())) {
                        List<Integer> asientos = Arrays.stream(asientosStr.split(";"))
                                .map(Integer::parseInt)
                                .collect(Collectors.toList());
                        if (asientos.contains(asiento)) return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer reservas: " + e.getMessage());
        }
        return false;
    }

    public static void actualizarMatrizDesdeReservas(Vuelo vuelo) {
        File archivo = new File("Datos/reservas.txt");

        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    String origenDestino = partes[1];

                    if (origenDestino.equalsIgnoreCase(vuelo.getOrigen() + " -> " + vuelo.getDestino())) {
                        for (int i = 2; i < partes.length; i++) {
                            try {
                                int asiento = Integer.parseInt(partes[i].trim());
                                if (asiento >= 1 && asiento <= vuelo.getCapacidad()) {
                                    vuelo.getAsientos()[asiento - 1] = true;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Asiento inválido en línea: " + linea);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al actualizar matriz: " + e.getMessage());
        }
    }

    public static void eliminarReserva(Pasajero pasajero, Vuelo vuelo) {
        File archivoOriginal = new File("Datos/reservas.txt");
        File archivoTemporal = new File("Datos/reservas_temp.txt");

        if (!archivoOriginal.exists()) return;

        try (
                BufferedReader br = new BufferedReader(new FileReader(archivoOriginal));
                BufferedWriter bw = new BufferedWriter(new FileWriter(archivoTemporal))
        ) {
            String linea;
            String clave = pasajero.getNombre().trim() + ";" + vuelo.getOrigen().trim() + " -> " + vuelo.getDestino().trim();

            while ((linea = br.readLine()) != null) {
                if (!linea.startsWith(clave)) {
                    bw.write(linea);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al eliminar reserva: " + e.getMessage());
            return;
        }

        // Reemplazar el archivo original con el temporal
        if (archivoOriginal.delete()) {
            archivoTemporal.renameTo(archivoOriginal);
        } else {
            System.out.println("No se pudo eliminar el archivo original de reservas.");
        }
    }








    public static void eliminarVueloDeArchivo(String idVueloAEliminar, List<Vuelo> vuelos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Datos/vuelos.txt"))) {
            for (Vuelo vuelo : vuelos) {
                if (!vuelo.getId().equals(idVueloAEliminar)) {
                    String estado = vuelo.isCancelado() ? "OFFLINE" : "ONLINE";
                    bw.write(estado + ";" + vuelo.getId() + ";" + vuelo.getDia() + ";" + vuelo.getOrigen() + ";" +
                            vuelo.getDestino() + ";" + vuelo.getCapacidad() + ";" + vuelo.getHora() + ";" + vuelo.getPrecio());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al actualizar archivo de vuelos: " + e.getMessage());
        }
    }
    public static void actualizarArchivoDeVuelos(List<Vuelo> vuelos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Datos/vuelos.txt"))) {
            // Recorremos la lista de vuelos y los escribimos en el archivo
            for (Vuelo vuelo : vuelos) {
                String estado = vuelo.isCancelado() ? "OFFLINE" : "ONLINE";
                // Escribir la información del vuelo en el formato deseado
                bw.write(estado + ";" + vuelo.getId() + ";" + vuelo.getDia() + ";" +
                        vuelo.getOrigen() + ";" + vuelo.getDestino() + ";" +
                        vuelo.getCapacidad() + ";" + vuelo.getHora() + ";" + vuelo.getPrecio());
                bw.newLine();  // Para agregar una nueva línea entre los vuelos
            }
        } catch (IOException e) {
            System.out.println("Error al actualizar archivo: " + e.getMessage());
        }
    }
}
