package app;

import modelo.*;
import sistema.SistemaReservas;

import static sistema.SistemaScanner.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SistemaReservas sistema = new SistemaReservas();

        int opcion;
        do {
            System.out.println("\n--- Bienvenido a Aerolínea Patito Volador ---");
            System.out.println("1. Empleado");
            System.out.println("2. Cliente");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = (int) Sc(int.class);

            switch (opcion) {
                case 1 -> menuLoginEmpleado(sistema);
                case 2 -> menuLoginPasajero(sistema);
                case 3 -> {
                    System.out.println("Gracias por usar el sistema.");
                    System.exit(0);
                }
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 3);
    }

    private static void menuLoginEmpleado(SistemaReservas sistema) {
        Empleado empleado = sistema.iniciarSesionEmpleado();
        if (empleado != null){
            sistema.menuEmpleado(empleado);
        }
    }

    private static void menuLoginPasajero(SistemaReservas sistema) {
        Pasajero pasajero = sistema.iniciarSesionPasajero();
        if (pasajero != null){
            sistema.menuPasajero(pasajero);
        }
    }
}
