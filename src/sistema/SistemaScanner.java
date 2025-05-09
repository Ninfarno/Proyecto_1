package sistema;

import java.util.Scanner;

public class SistemaScanner {

    private static Scanner scanner = new Scanner(System.in);

    public static Object Sc(Class<?> Tipo) {
        while (true) {
            String Respuesta = scanner.nextLine();
            try {
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
