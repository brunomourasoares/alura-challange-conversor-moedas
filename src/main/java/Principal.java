import service.ServicoTaxaCambio;
import ui.MenuConsole;

public class Principal {
    public static void main(String[] args) {
        try {
            ServicoTaxaCambio servicoTaxaCambio = new ServicoTaxaCambio();
            MenuConsole menuConsole = new MenuConsole(servicoTaxaCambio);
            menuConsole.exibirMenu();
        } catch (RuntimeException e) {
            System.out.println("[ERRO] " + e.getMessage());
            System.out.println("\nPara usar o conversor, defina a variável de ambiente EXCHANGE_RATE_API_KEY ou edite src/main/resources/application.properties com sua chave da Exchange Rate API.");
            System.exit(1);
        }
    }
}
