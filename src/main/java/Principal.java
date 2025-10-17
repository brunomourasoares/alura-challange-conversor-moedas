import service.ServicoTaxaCambio;
import ui.MenuConsole;

public class Principal {
    public static void main(String[] args) {
        ServicoTaxaCambio servicoTaxaCambio = new ServicoTaxaCambio();
        MenuConsole menuConsole = new MenuConsole(servicoTaxaCambio);
        menuConsole.exibirMenu();
    }
}
