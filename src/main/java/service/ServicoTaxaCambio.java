package service;

import com.google.gson.Gson;
import model.ResultadoConversaoMoeda;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.util.Properties;
import java.io.InputStream;

public class ServicoTaxaCambio {
    private static final String URL_BASE = "https://v6.exchangerate-api.com/v6/";
    private final Gson gson = new Gson();
    private final String apiKey;

    public ServicoTaxaCambio() {
        this.apiKey = obterApiKey();
    }

    public ResultadoConversaoMoeda converter(String moedaOrigem, String moedaDestino, double valorParaConverter) throws Exception {
        String respostaJson = obterResposta(moedaOrigem);
        RespostaTaxaCambio resposta = gson.fromJson(respostaJson, RespostaTaxaCambio.class);

        if (resposta.conversion_rates == null) {
            throw new RuntimeException("Resposta inesperada da API: " + respostaJson);
        }

        Double taxa = resposta.conversion_rates.get(moedaDestino);

        if (taxa == null) {
            throw new RuntimeException("Moeda de destino não encontrada na resposta: " + respostaJson);
        }

        double valorConvertido = valorParaConverter * taxa;
        return new ResultadoConversaoMoeda(moedaOrigem, moedaDestino, valorParaConverter, valorConvertido, taxa);
    }

    private String obterResposta(String moedaOrigem) throws IOException {
        String urlApi = URL_BASE + apiKey + "/latest/" + moedaOrigem;
        URL url = new URL(urlApi);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");
        conexao.connect();

        if (conexao.getResponseCode() != 200) {
            throw new RuntimeException("Falha ao obter taxas de câmbio");
        }

        BufferedReader leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
        StringBuilder respostaBuilder = new StringBuilder();
        String linha;

        while ((linha = leitor.readLine()) != null) {
            respostaBuilder.append(linha);
        }

        return respostaBuilder.toString();
    }

    private String obterApiKey() {
        String chave = System.getenv("EXCHANGE_RATE_API_KEY");

        if (chave != null && !chave.isBlank()) {
            return chave.trim();
        }

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                chave = prop.getProperty("exchange.rate.api.key");

                if (chave != null && !chave.isBlank() && !chave.equals("SUA_API_KEY_AQUI")) {
                    return chave.trim();
                }

            }
        } catch (IOException e) {
        }

        throw new RuntimeException("API key não encontrada. Defina a variável de ambiente EXCHANGE_RATE_API_KEY ou configure em src/main/resources/application.properties (exchange.rate.api.key)");
    }

    private static class RespostaTaxaCambio {
        public java.util.Map<String, Double> conversion_rates;
    }
}
