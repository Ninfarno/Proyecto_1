package sistema;

import modelo.*;
import java.io.*;
import java.util.*;

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
    public static void guardarPasajeroEnArchivo(Pasajero p) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Datos/pasajeros.txt", true))) {
            bw.write(p.obtenerLineaArchivo());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar pasajero: " + e.getMessage());
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
                    vuelo.getDestino() + ";" + vuelo.getCapacidad() + ";" + vuelo.getHora());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar el vuelo en el archivo: " + e.getMessage());
        }
    }
    public static void eliminarVueloDeArchivo(String idVueloAEliminar, List<Vuelo> vuelos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Datos/vuelos.txt"))) {
            for (Vuelo vuelo : vuelos) {
                if (!vuelo.getId().equals(idVueloAEliminar)) {
                    String estado = vuelo.isCancelado() ? "OFFLINE" : "ONLINE";
                    bw.write(estado + ";" + vuelo.getId() + ";" + vuelo.getDia() + ";" + vuelo.getOrigen() + ";" +
                            vuelo.getDestino() + ";" + vuelo.getCapacidad() + ";" + vuelo.getHora());
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
                        vuelo.getCapacidad() + ";" + vuelo.getHora());
                bw.newLine();  // Para agregar una nueva línea entre los vuelos
            }
        } catch (IOException e) {
            System.out.println("Error al actualizar archivo: " + e.getMessage());
        }
    }
}
