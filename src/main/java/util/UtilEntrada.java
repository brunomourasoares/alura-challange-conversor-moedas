package util;

import java.util.Scanner;

public class UtilEntrada {
    private static final Scanner scanner = new Scanner(System.in);

    public static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    public static double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException excecao) {
                System.out.println("Valor inválido. Tente novamente.");
            }
        }
    }
}
