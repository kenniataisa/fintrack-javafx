package utils;

public class Formatador {

    public static String formatarValorMonetario(double valor) {
        return "R$ " + String.format("%.2f", valor);
    }
}