package sistema;

import java.util.Scanner;

public class SistemaScanner {

    public static Scanner scanner = new Scanner(System.in);
    public static class EsperarEnter{
    }
    public static Object Sc(Class<?> Tipo) {
        while (true) {
            try {
                if (Tipo == EsperarEnter.class) {
                    System.out.println("Presione enter para continuar... ");
                    scanner.nextLine(); // Espera la entrada del usuario
                    return null;
                }
                String Respuesta = scanner.nextLine();
                if (Tipo == String.class) {
                    return Respuesta;
                } else if (Tipo == double.class) {
                    return Double.parseDouble(Respuesta);
                } else if (Tipo == int.class) {
                    return Integer.parseInt(Respuesta);
                } else if (Tipo == boolean.class) {
                    if(Respuesta.equalsIgnoreCase("si")){
                        return true;
                    } else if (Respuesta.equalsIgnoreCase("no")){
                        return false;
                    }
                    System.out.println("Por favor responda 'si' 'no'");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor escriba la respuesta esperada");
                continue;
            }
        }
    }
}
