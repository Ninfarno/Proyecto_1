package modelo;

import java.util.*;

public class Pasajero extends Usuario {
    private Map<String, List<Integer>> vuelosTomados;
    private String dni;
    public String getDni() {
        return dni;
    }


    public Pasajero(String nombre, String usuario, String contrasena) {
        super(nombre, usuario, contrasena);
        this.vuelosTomados = new HashMap<>();
    }

    public static Pasajero desdeLineaArchivo(String linea) {
        String[] partes = linea.split(";");
        if (partes.length < 3) return null;

        String nombre = partes[0];
        String correo = partes[1];
        String dni = partes[2];

        return new Pasajero(nombre, correo, dni); // Ajusta si tienes más campos
    }


    public void agregarVueloTomado(String vueloId, List<Integer> asientos) {
        vuelosTomados.computeIfAbsent(vueloId, k -> new ArrayList<>()).addAll(asientos);
    }

    public Map<String, List<Integer>> getVuelosTomados() {
        return vuelosTomados;
    }

    public void setVuelosTomadosDesdeTexto(String[] partes) {
        for (int i = 3; i < partes.length; i++) {
            String[] subpartes = partes[i].split(":");
            if (subpartes.length >= 2) {
                String vueloId = subpartes[0];
                List<Integer> asientos = new ArrayList<>();
                for (int j = 1; j < subpartes.length; j++) {
                    try {
                        asientos.add(Integer.parseInt(subpartes[j]));
                    } catch (NumberFormatException ignored) {
                    }
                }
                vuelosTomados.put(vueloId, asientos);
            }
        }
    }

    public String obtenerLineaArchivo() {
        StringBuilder sb = new StringBuilder();
        sb.append(getNombre()).append(";")
                .append(getUsuario()).append(";")
                .append(getContrasena());

        for (Map.Entry<String, List<Integer>> entrada : vuelosTomados.entrySet()) {
            sb.append(";").append(entrada.getKey());
            for (int asiento : entrada.getValue()) {
                sb.append(":").append(asiento);
            }
        }

        return sb.toString();
    }

    public void verReservas() {
        if (vuelosTomados.isEmpty()) {
            System.out.println("No tiene reservas realizadas.");
        } else {
            System.out.println("Reservas de " + getNombre() + ":");
            for (Map.Entry<String, List<Integer>> entry : vuelosTomados.entrySet()) {
                String vueloId = entry.getKey();
                List<Integer> asientos = entry.getValue();
                System.out.print("Vuelo " + vueloId + " - Asientos reservados: ");

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < asientos.size(); i++) {
                    sb.append(asientos.get(i));
                    if (i < asientos.size() - 1) {
                        sb.append(", ");
                    }
                }
                System.out.println(sb.toString());
            }
        }
    }

    public void cancelarVuelo(String idVuelo) {
        if (vuelosTomados.containsKey(idVuelo)) {
            vuelosTomados.remove(idVuelo);
            System.out.println("Reserva del vuelo " + idVuelo + " cancelada exitosamente.");
        } else {
            System.out.println("No se encontró ninguna reserva para el vuelo con ID: " + idVuelo);
        }
    }
}
