package modelo;

import java.util.*;
import java.util.stream.Collectors;

public class Ticket {
    private Vuelo vuelo;
    private Pasajero pasajero;
    private List<Integer> asientosReservados;

    public Ticket(Vuelo vuelo, Pasajero pasajero, List<Integer> asientosReservados) {
        this.vuelo = vuelo;
        this.pasajero = pasajero;
        this.asientosReservados = new ArrayList<>(asientosReservados);
    }

    public void imprimir() {
        System.out.println("----- Ticket de Reserva -----");
        System.out.println("Pasajero: " + pasajero.getNombre());
        System.out.println("Vuelo: " + vuelo.getOrigen() + " → " + vuelo.getDestino());
        System.out.println("Lugares reservados: " +
                asientosReservados.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", ")));
        System.out.println("---<Total: $" + asientosReservados.stream()
                .mapToDouble(asiento -> vuelo.getPrecioAsiento(asiento))
                .sum() + ">---");
        System.out.println("----------------------------------------------");
        System.out.println("Este ticket sera su boleto para abordar su\n" +
                "respectivo vuelo, si ha perdido su vuelo no\n" +
                "habra reembolso y la empresa no se hace\n" +
                "responsable de perdidas durante su vuelo\n");
        System.out.println("Gracias por su compra :D");
    }
}
