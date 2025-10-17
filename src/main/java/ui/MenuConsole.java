package ui;

import model.ResultadoConversaoMoeda;
import model.ConversaoLog;
import service.ServicoTaxaCambio;
import util.UtilEntrada;
import java.text.NumberFormat;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class MenuConsole {
    private static final String ARQUIVO_HISTORICO = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "historico-conversoes.csv";
    private final ServicoTaxaCambio servico;
    private final List<ConversaoLog> historico = new ArrayList<>();
    private final Map<String, String> moedasSuportadas;

    public MenuConsole(ServicoTaxaCambio servico) {
        this.servico = servico;

        // Usando TreeMap para garantir a ordem alfabética das chaves (códigos das moedas)
        this.moedasSuportadas = new TreeMap<>();
        this.moedasSuportadas.put("USD", "Dólar Americano");
        this.moedasSuportadas.put("BRL", "Real Brasileiro");
        this.moedasSuportadas.put("ARS", "Peso Argentino");
        this.moedasSuportadas.put("COP", "Peso Colombiano");
        this.moedasSuportadas.put("EUR", "Euro");
        this.moedasSuportadas.put("GBP", "Libra Esterlina");
        this.moedasSuportadas.put("JPY", "Iene Japonês");
        this.moedasSuportadas.put("CHF", "Franco Suíço");
        this.moedasSuportadas.put("CAD", "Dólar Canadense");
        this.moedasSuportadas.put("AUD", "Dólar Australiano");
        this.moedasSuportadas.put("CNY", "Yuan Chinês");
        this.moedasSuportadas.put("MXN", "Peso Mexicano");

        carregarHistoricoDeArquivo();
    }

    public void exibirMenu() {
        while (true) {
            System.out.println("""
                    *****************************************************
                    Seja bem-vindo(a) ao Conversor de Moedas =]
                    
                    1- Converter moedas
                    2- Exibir histórico de conversões
                    3- Sair
                    *****************************************************
                    """);
            String opcao = UtilEntrada.lerTexto("Escolha uma opção: ");
            switch (opcao) {
                case "1": realizarConversaoDinamica(); break;
                case "2": exibirHistorico(); break;
                case "3": System.out.println("Saindo..."); return;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    private void realizarConversaoDinamica() {
        System.out.println("Moedas disponíveis:");
        int idx = 1;
        // A lista de códigos já virá ordenada do TreeMap
        List<String> codigos = new ArrayList<>(moedasSuportadas.keySet());
        for (String codigo : codigos) {
            System.out.printf("%d - %s (%s)\n", idx++, moedasSuportadas.get(codigo), codigo);
        }
        int escolhaOrigem = lerIndiceMoeda("Escolha a moeda de origem (número): ", codigos.size());
        int escolhaDestino = lerIndiceMoeda("Escolha a moeda de destino (número): ", codigos.size());
        String moedaOrigem = codigos.get(escolhaOrigem - 1);
        String moedaDestino = codigos.get(escolhaDestino - 1);
        double valorParaConverter = UtilEntrada.lerDouble("Digite o valor que deseja converter: ");
        try {
            ResultadoConversaoMoeda resultado = servico.converter(moedaOrigem, moedaDestino, valorParaConverter);
            String valorOriginalFormatado = formatarMoeda(resultado.valorOriginal(), moedaOrigem) + " [" + moedaOrigem + "]";
            String valorConvertidoFormatado = formatarMoeda(resultado.valorConvertido(), moedaDestino) + " [" + moedaDestino + "]";
            System.out.printf("Valor %s corresponde ao valor final de =>>> %s\n\n",
                    valorOriginalFormatado, valorConvertidoFormatado);
            ConversaoLog log = new ConversaoLog(
                LocalDateTime.now(), moedaOrigem, moedaDestino, resultado.valorOriginal(), resultado.valorConvertido(), resultado.taxa()
            );
            historico.add(log);
            salvarConversaoNoArquivo(log);
        } catch (Exception excecao) {
            System.out.println("Erro na conversão: " + excecao.getMessage());
        }
    }

    private int lerIndiceMoeda(String mensagem, int max) {
        while (true) {
            int escolha = (int) UtilEntrada.lerDouble(mensagem);
            if (escolha >= 1 && escolha <= max) return escolha;
            System.out.println("Escolha inválida. Tente novamente.");
        }
    }

    private void exibirHistorico() {
        List<ConversaoLog> historicoArquivo = lerHistoricoDoArquivo();
        if (historicoArquivo.isEmpty()) {
            System.out.println("Nenhuma conversão realizada ainda.\n");
            return;
        }
        System.out.println("\n=== Histórico de Conversões ===");
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        for (ConversaoLog log : historicoArquivo) {
            String valorOriginal = formatarMoeda(log.valorOriginal(), log.moedaOrigem()) + " [" + log.moedaOrigem() + "]";
            String valorConvertido = formatarMoeda(log.valorConvertido(), log.moedaDestino()) + " [" + log.moedaDestino() + "]";
            String dataFormatada = log.dataHora().format(formatoData);
            System.out.printf("%s | %s => %s | %s => %s | Taxa: %.6f\n",
                dataFormatada,
                moedasSuportadas.getOrDefault(log.moedaOrigem(), log.moedaOrigem()),
                moedasSuportadas.getOrDefault(log.moedaDestino(), log.moedaDestino()),
                valorOriginal,
                valorConvertido,
                log.taxa()
            );
        }
        System.out.println();
    }

    private void salvarConversaoNoArquivo(ConversaoLog log) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.000000", symbols);

        String linha = String.join(",",
            log.dataHora().format(formatoData),
            log.moedaOrigem(),
            log.moedaDestino(),
            df.format(log.valorOriginal()),
            df.format(log.valorConvertido()),
            df.format(log.taxa())
        ) + "\n";

        try {
            Path caminho = Paths.get(ARQUIVO_HISTORICO);
            if (!Files.exists(caminho.getParent())) {
                Files.createDirectories(caminho.getParent());
            }
            if (!Files.exists(caminho)) {
                Files.createFile(caminho);
            }
            Files.write(
                caminho,
                linha.getBytes(),
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.out.println("[ERRO] Não foi possível salvar o histórico em arquivo: " + e.getMessage());
        }
    }

    private List<ConversaoLog> lerHistoricoDoArquivo() {
        List<ConversaoLog> lista = new ArrayList<>();
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Path caminho = Paths.get(ARQUIVO_HISTORICO);
        if (!Files.exists(caminho)) {
            return lista;
        }
        try {
            List<String> linhas = Files.readAllLines(caminho);
            for (String linha : linhas) {
                String[] partes = linha.split(",");
                if (partes.length == 6) {
                    ConversaoLog log = new ConversaoLog(
                        LocalDateTime.parse(partes[0], formatoData),
                        partes[1], partes[2],
                        Double.parseDouble(partes[3]),
                        Double.parseDouble(partes[4]),
                        Double.parseDouble(partes[5])
                    );
                    lista.add(log);
                }
            }
        } catch (IOException e) {
            System.out.println("[ERRO] Não foi possível ler o histórico do arquivo: " + e.getMessage());
        }
        return lista;
    }

    private void carregarHistoricoDeArquivo() {
        try {
            historico.clear();
            historico.addAll(lerHistoricoDoArquivo());
        } catch (Exception e) {
            System.out.println("[ERRO] Falha ao carregar o histórico: " + e.getMessage());
        }
    }

    private String formatarMoeda(double valor, String codigoMoeda) {
        Locale local = switch (codigoMoeda) {
            case "BRL" -> new Locale("pt", "BR");
            case "ARS" -> new Locale("es", "AR");
            case "COP" -> new Locale("es", "CO");
            case "EUR" -> Locale.GERMANY;
            case "GBP" -> Locale.UK;
            case "JPY" -> Locale.JAPAN;
            case "CHF" -> new Locale("de", "CH");
            case "CAD" -> new Locale("en", "CA");
            case "AUD" -> new Locale("en", "AU");
            case "CNY" -> Locale.CHINA;
            case "MXN" -> new Locale("es", "MX");
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
