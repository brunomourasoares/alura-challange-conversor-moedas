package ui;

import model.ResultadoConversaoMoeda;
import service.ServicoTaxaCambio;
import util.UtilEntrada;
import java.text.NumberFormat;
import java.util.Locale;

public record MenuConsole(ServicoTaxaCambio servico) {

    public void exibirMenu() {
        while (true) {
            System.out.println("""
                    *****************************************************
                    Seja bem-vindo(a) ao Conversor de Moedas =]
                    
                    1- Dólar =>> Peso Argentino
                    2- Peso Argentino =>> Dólar
                    3- Dólar =>> Real brasileiro
                    4- Real brasileiro =>> Dólar
                    5- Dólar =>> Peso Colombiano
                    6- Peso Colombiano =>> Dólar
                    7- Sair
                    
                    Escolha uma opção válida:
                    *****************************************************
                    """);

            String opcao = UtilEntrada.lerTexto("");

            switch (opcao) {
                case "1": realizarConversao("USD", "ARS"); break;
                case "2": realizarConversao("ARS", "USD"); break;
                case "3": realizarConversao("USD", "BRL"); break;
                case "4": realizarConversao("BRL", "USD"); break;
                case "5": realizarConversao("USD", "COP"); break;
                case "6": realizarConversao("COP", "USD"); break;
                case "7": System.out.println("Saindo..."); return;
                default:
                    System.out.println("Opção inválida.");
                    opcao = UtilEntrada.lerTexto("Escolha uma opção válida: ");
            }
        }
    }

    private void realizarConversao(String moedaOrigem, String moedaDestino) {
        double valorParaConverter = UtilEntrada.lerDouble("Digite o valor que deseja converter: ");
        try {
            ResultadoConversaoMoeda resultado = servico.converter(moedaOrigem, moedaDestino, valorParaConverter);
            String valorOriginalFormatado = formatarMoeda(resultado.valorOriginal(), moedaOrigem) + " [" + moedaOrigem + "]";
            String valorConvertidoFormatado = formatarMoeda(resultado.valorConvertido(), moedaDestino) + " [" + moedaDestino + "]";
            System.out.printf("Valor %s corresponde ao valor final de =>>> %s\n\n",
                    valorOriginalFormatado, valorConvertidoFormatado);
        } catch (Exception excecao) {
            System.out.println("Erro na conversão: " + excecao.getMessage());
        }
    }

    private String formatarMoeda(double valor, String codigoMoeda) {
        Locale local = switch (codigoMoeda) {
            case "BRL" -> new Locale("pt", "BR");
            case "ARS" -> new Locale("es", "AR");
            case "COP" -> new Locale("es", "CO");
            default -> Locale.US;
        };
        NumberFormat formato = NumberFormat.getCurrencyInstance(local);
        String valorFormatado = formato.format(valor);

        if ("USD".equals(codigoMoeda)) {
            valorFormatado = valorFormatado.replace("$", "US$");
        }

        return valorFormatado;
    }
}
