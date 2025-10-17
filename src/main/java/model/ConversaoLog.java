package model;

import java.time.LocalDateTime;

public record ConversaoLog(
    LocalDateTime dataHora,
    String moedaOrigem,
    String moedaDestino,
    double valorOriginal,
    double valorConvertido,
    double taxa
) {}

