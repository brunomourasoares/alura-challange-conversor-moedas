package model;

public record ResultadoConversaoMoeda(String moedaOrigem, String moedaDestino, double valorOriginal,
                                      double valorConvertido, double taxa) {
}