package modelo;

import java.util.*;

public class Vuelo {
    private static int contador = 1;
    private String id;
    private String origen;
    private String destino;
    private String dia;
    private String hora;
    private int capacidad;
    private int ocupados;
    private boolean[] asientosOcupados;
    private double[] Precio;
    private boolean[] asientos;
    private boolean cancelado = false;
    private String motivoCancelacion;
    private List<Ticket> tickets;

    public Vuelo(String id, String dia, String origen, String destino, int capacidad, String hora, double precio) {
        this.id = id;
        this.dia = dia;
        this.hora = hora;
        this.origen = origen;
        this.destino = destino;
        this.capacidad = capacidad;
        this.asientos = new boolean[capacidad];
        this.cancelado = false;
        this.asientosOcupados = new boolean[capacidad];
        this.Precio = new double[capacidad];
        this.tickets = new ArrayList<>();

        for (int i = 0; i < capacidad; i++) {
            Precio[i] = precio;
        }
    }

    public boolean estaDisponible(int lugares) {
        return !cancelado && (ocupados + lugares <= capacidad);
    }

    // Primera version de Ticket con las reservas solo contenia cantidad de asientos no cuales
    /*public Ticket reservar(Pasajero pasajero, int lugares) {
        if (!estaDisponible(lugares)) return null;
        ocupados += lugares;
        Ticket ticket = new Ticket(this, pasajero, lugares);
        tickets.add(ticket);
        return ticket;
    }*/

    public void cancelar(String motivo) {
        this.cancelado = true;
        this.motivoCancelacion = motivo;
    }

    public String getPrecio() {
        return Precio[contador] + "";
    }

    public String getId() {
        return id;
    }

    public String getDia() {
        return dia;
    }

    public String getHora() {
        return hora;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public int getOcupados() {
        return ocupados;
    }

    public void setOcupados(int ocupados) {
        this.ocupados = ocupados;
    }

    public boolean[] getAsientos() {
        return asientos;
    }

    public void cancelar() {
        this.cancelado = true;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public double getPrecioAsiento(int numero) {
        return Precio[numero - 1];
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public Ticket reservar(Pasajero pasajero, List<Integer> asientosSeleccionados) {
        if (cancelado) return null;

        for (int asiento : asientosSeleccionados) {
            if (asiento < 1 || asiento > capacidad || asientos[asiento - 1]) {
                return null; // Asiento inválido o ya ocupado
            }
        }

        for (int asiento : asientosSeleccionados) {
            asientos[asiento - 1] = true;
            ocupados++;
        }

        Ticket ticket = new Ticket(this, pasajero, asientosSeleccionados);
        tickets.add(ticket);
        return ticket;
    }

    public void iniciarOcupacionAleatoria() {
        new Thread(() -> {
            Random random = new Random();
            while (!cancelado && ocupados < capacidad) {
                int intentos = random.nextInt(5) + 1;

                for (int i = 0; i < intentos; i++) {
                    int asiento = random.nextInt(capacidad);
                    if (!asientos[asiento]) {
                        asientos[asiento] = true;
                        ocupados++;
                    }
                    if (ocupados >= capacidad) break;
                }

                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    System.out.println("Hilo interrumpido: " + e.getMessage());
                    break;
                }
            }
        }).start();
    }

    @Override
    public String toString() {
        String estado = cancelado ? "[Cancelado]" : "[Disponible]";
        return estado + " Vuelo " + id + " - " + dia + " a las " + hora + " - " + origen + " → " + destino +
                " | Asientos Disponibles: " + (capacidad - ocupados) + "/" + capacidad;
    }
}
